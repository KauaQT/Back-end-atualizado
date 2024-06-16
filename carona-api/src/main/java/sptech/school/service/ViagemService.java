package sptech.school.service;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sptech.school.dto.*;
import sptech.school.entity.*;
import sptech.school.repository.*;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service

@RequiredArgsConstructor
public class ViagemService {

    private static final String GOOGLE_MAPS_API_KEY = "AIzaSyBCgrMgCudI7Jcc3xd8DDZAlqb8_7lWvF4";

    @Autowired
    private UsuarioService usuarioService;


    @Autowired
    private final ViagemRepository viagemRepository;


    @Autowired
    private EnderecoRepository enderecoRepository;


    @Autowired
    private PartidaRepository partidaRepository;

    @Autowired
    private DestinoRepository destinoRepository;

    @Autowired
    private CarroRepository carroRepository;


    @Autowired
    private UsuarioRespository usuarioRepository;
    @Autowired
    private GeocodingService geocodingService;

    private static final String GOOGLE_MAPS_URL = "https://maps.googleapis.com/maps/api/geocode/json?latlng=%s,%s&key=AIzaSyDT1W9aIUuRBEbdOT82TS_Fr5HxapN5SqA";

    private static final String GOOGLE_MAPS_DISTANCIA = "https://maps.googleapis.com/maps/api/directions/json?origin=%s&destination=%s&key=AIzaSyCjfdkq8uHIzU3mr1OZqAtkJggC-k2PZdo";


    @Transactional
    public RetornoViagemDto saveViagem(ViagemDTO viagemDTO) {
        Objects.requireNonNull(viagemDTO, "ViagemDTO não pode ser nulo");

        Usuario motorista = usuarioRepository.findById(viagemDTO.getIdMotorista())
                .orElseThrow(() -> new IllegalArgumentException("Motorista não encontrado para o ID: " + viagemDTO.getIdMotorista()));

        if (viagemDTO.getPassageiros() != null && viagemDTO.getPassageiros().contains(viagemDTO.getIdMotorista())) {
            throw new IllegalArgumentException("O motorista não pode cadastrar ele mesmo na viagem.");
        }

        if (viagemDTO.getLatitudePontoPartida() == null || viagemDTO.getLongitudePontoPartida() == null ||
                viagemDTO.getLatitudePontoDestino() == null || viagemDTO.getLongitudePontoDestino() == null) {
            throw new IllegalArgumentException("Latitude ou longitude não pode ser nula");
        }

        Carro carro = carroRepository.findById(viagemDTO.getIdCarro())
                .orElseThrow(() -> new IllegalArgumentException("Carro não encontrado para o ID: " + viagemDTO.getIdCarro()));

        Partida partida = new Partida();
        partida.setPontoPartida(extrairEndereco(buscarEndereco(viagemDTO.getLatitudePontoPartida(), viagemDTO.getLongitudePontoPartida())));
        partida = partidaRepository.save(partida);

        Destino destino = (Destino) destinoRepository.findByPontoDestino(extrairEndereco(buscarEndereco(viagemDTO.getLatitudePontoDestino(), viagemDTO.getLongitudePontoDestino())))
                .orElseGet(() -> {
                    Destino novoDestino = new Destino();
                    novoDestino.setPontoDestino(extrairEndereco(buscarEndereco(viagemDTO.getLatitudePontoDestino(), viagemDTO.getLongitudePontoDestino())));
                    return destinoRepository.save(novoDestino);
                });

        String tempoMedioViagemStr = "";
        try {
            tempoMedioViagemStr = calcularTempoViagem(partida.getPontoPartida(), destino.getPontoDestino());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        int horas = 0;
        int minutos = 0;

        Pattern pattern = Pattern.compile("(\\d+) horas e (\\d+) minutos");
        Matcher matcher = pattern.matcher(tempoMedioViagemStr);

        if (matcher.find()) {
            horas = Integer.parseInt(matcher.group(1));
            minutos = Integer.parseInt(matcher.group(2));
        }

        int tempoTotalMinutos = horas * 60 + minutos;

        Viagem viagem = new Viagem();
        viagem.setPartida(partida);
        viagem.setDestino(destino);
        viagem.setValor(viagemDTO.getValor());
        viagem.setHorarioViagem(viagemDTO.getHorario());
        viagem.setSoMulheres(viagemDTO.getSoMulheres());
        viagem.setMotorista(motorista);
        viagem.setCarro(carro);
        viagem.setTempoMedioViagem(tempoTotalMinutos);
        viagem.setDiaViagem(viagemDTO.getDiaViagem());
        viagem.setQntPassageiros(viagemDTO.getQntPassageiros());
        viagem.setStatus(Viagem.StatusViagem.PENDENTE);

        if (viagemDTO.getPassageiros() != null && !viagemDTO.getPassageiros().isEmpty()) {
            for (Integer passageiroId : viagemDTO.getPassageiros()) {
                Usuario passageiro = usuarioRepository.findById(passageiroId)
                        .orElseThrow(() -> new IllegalArgumentException("Passageiro não encontrado para o ID: " + passageiroId));

                viagem.addPassageiro(passageiro);
            }
        }

        viagem = viagemRepository.save(viagem);

        RetornoViagemDto retornoViagemDto = new RetornoViagemDto();
        retornoViagemDto.setIdViagem(viagem.getIdViagem());
        retornoViagemDto.setEnderecoPartida(partida.getPontoPartida());
        retornoViagemDto.setEnderecoDestino(destino.getPontoDestino());
        retornoViagemDto.setHorario(viagemDTO.getHorario());
        retornoViagemDto.setValor(viagemDTO.getValor());
        retornoViagemDto.setQntPassageiros(viagemDTO.getQntPassageiros());
        retornoViagemDto.setSoMulheres(viagemDTO.getSoMulheres());
        retornoViagemDto.setIdCarro(viagemDTO.getIdCarro());
        retornoViagemDto.setTempoMedioViagem(tempoTotalMinutos);
        retornoViagemDto.setDiaViagem(viagemDTO.getDiaViagem());

        return retornoViagemDto;
    }


    public List<Viagem> listarViagensPorMotorista(Usuario motorista) {
        return viagemRepository.findByMotorista(motorista);
    }

    public int contarViagensComPassageiro(Usuario motorista, Usuario passageiro) {
        return viagemRepository.countByMotoristaAndListaPassageiros(motorista, passageiro);
    }

      @Transactional
      public void finalizarViagem(Integer idViagem, Integer idMotorista) {
          Objects.requireNonNull(idViagem, "ID da viagem não pode ser nulo");
          Objects.requireNonNull(idMotorista, "ID do motorista não pode ser nulo");

          Viagem viagem = viagemRepository.findById(idViagem)
                  .orElseThrow(() -> new IllegalArgumentException("Viagem não encontrada para o ID: " + idViagem));


          if (!(viagem.getMotorista().getId() == idMotorista)) {
              throw new IllegalArgumentException("Apenas o motorista que criou a viagem pode finalizá-la.");
          }

          viagem.setStatus(Viagem.StatusViagem.FINALIZADA);

          viagemRepository.save(viagem);
      }

    public List<Viagem> buscarPassageirosFidelizadosPorMotorista(Integer idMotorista) {
        return null;
    }
    public static String calcularTempoViagem(String origem , String destino) throws UnsupportedEncodingException {
        Logger logger = LoggerFactory.getLogger(ViagemService.class);

        logger.info("Iniciando cálculo de tempo de viagem.");

        if (origem == null || destino == null) {
            logger.error("Origem ou destino não pode ser nulo.");
            throw new IllegalArgumentException("Origem ou destino não pode ser nulo.");
        }

        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyCjfdkq8uHIzU3mr1OZqAtkJggC-k2PZdo")
                .build();

        DirectionsApiRequest request = DirectionsApi.getDirections(context, origem, destino);

        request.mode(TravelMode.DRIVING);

        DirectionsResult result = null;
        try {
            result = request.await();
        } catch (Exception e) {
            logger.error("Erro ao calcular tempo de viagem: {}", e.getMessage());
            throw new RuntimeException("Erro ao calcular tempo de viagem", e);
        }

        DirectionsRoute[] routes = result.routes;
        if (routes.length == 0) {
            logger.error("Nenhuma rota encontrada.");
            throw new IllegalArgumentException("Nenhuma rota encontrada.");
        }

        long duration = routes[0].legs[0].duration.inSeconds;

        long hours = TimeUnit.SECONDS.toHours(duration);
        long minutes = TimeUnit.SECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(hours);

        String tempoViagem = String.format("%d horas e %d minutos", hours, minutes);
        logger.info("Tempo de viagem calculado: {}", tempoViagem);
        return tempoViagem;
    }

    public String calcularTempoEstimadoViagem(String pontoPartida, String pontoDestino) {
        String url = String.format(GOOGLE_MAPS_DISTANCIA, pontoPartida, pontoDestino);
        String responseBody = "";

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, null);

            CloseableHttpClient httpClient = HttpClients.custom()
                    .setSslcontext(sslContext)
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .build();

            HttpGet request = new HttpGet(url);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    responseBody = EntityUtils.toString(entity);
                } else {
                    System.out.println("Falha na requisição: " + response.getStatusLine().getReasonPhrase());
                }
            }
        } catch (IOException | NoSuchAlgorithmException | KeyManagementException e) {
            System.err.println("Erro ao executar requisição HTTP: " + e.getMessage());
        }

        JSONObject json = new JSONObject(responseBody);

        JSONArray routes = json.getJSONArray("routes");

        JSONObject route = routes.getJSONObject(0);

        JSONArray legs = route.getJSONArray("legs");

        JSONObject leg = legs.getJSONObject(0);

        JSONObject duration = leg.getJSONObject("duration");

        String tempoEstimado = duration.getString("text");

        return tempoEstimado;
    }
    public String extrairEndereco(String jsonResponse) {
        int startIndex = jsonResponse.indexOf("\"formatted_address\" : \"");
        if (startIndex != -1) {
            int addressStartIndex = startIndex + "\"formatted_address\" : \"".length();

            int addressEndIndex = jsonResponse.indexOf("\"", addressStartIndex);

            if (addressEndIndex != -1) {
                return jsonResponse.substring(addressStartIndex, addressEndIndex);
            }
        }

        return "";
    }

    private String buscarEndereco(double latitude, double longitude) {
        String url = String.format(GOOGLE_MAPS_URL, latitude, longitude);
        String responseBody = "";

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, null);

            CloseableHttpClient httpClient = HttpClients.custom()
                    .setSslcontext(sslContext)
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .build();

            HttpGet request = new HttpGet(url);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    responseBody = EntityUtils.toString(entity);
                } else {
                    System.out.println("Falha na requisição: " + response.getStatusLine().getReasonPhrase());
                }
            }
        } catch (IOException | NoSuchAlgorithmException | KeyManagementException e) {
            System.err.println("Erro ao executar requisição HTTP: " + e.getMessage());
        }

        return responseBody;
    }

    public List<ListagemMotoristaViagem> buscarViagensProximas(Double latitudePartida, Double longitudePartida,
                                                               Double latitudeDestino, Double longitudeDestino,
                                                               LocalDate dataHoraViagem) {
        Logger logger = LoggerFactory.getLogger(getClass());
        double maxDistancia = 100.0; // Definir distância máxima aceitável

        String origem = extrairEndereco(buscarEndereco(latitudePartida, longitudePartida));
        String destino = extrairEndereco(buscarEndereco(latitudeDestino, longitudeDestino));

        logger.info("Origem: {}", origem);
        logger.info("Destino: {}", destino);

        List<Viagem> viagensParaHorario = viagemRepository.findViagensAndUsuariosByHorario(dataHoraViagem);

        logger.info("Consultou viagens para a data/hora da viagem: {}", dataHoraViagem);

        Map<Usuario, Double> distanciasMotoristas = new HashMap<>();

        for (Viagem viagem : viagensParaHorario) {
            String localizacaoMotorista = viagem.getPartida().getPontoPartida();

            try {
                double distancia = calcularDistancia(origem, localizacaoMotorista);
                if (distancia > maxDistancia) {
                    continue;
                }
                distanciasMotoristas.put(viagem.getMotorista(), distancia);
            } catch (Exception e) {
                logger.error("Erro ao calcular a distância entre origem e destino: {}", e.getMessage());
            }
        }

        List<Usuario> motoristasOrdenados = distanciasMotoristas.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        logger.info("Motoristas ordenados por distância: {}", motoristasOrdenados);

        List<ListagemMotoristaViagem> dtos = new ArrayList<>();
        for (Usuario motorista : motoristasOrdenados) {
            List<Viagem> viagens = motorista.getViagens();
            if (viagens != null && !viagens.isEmpty()) {
                Viagem viagem = viagens.get(viagens.size() - 1);

                double tempoEntreViagem = 0.0;
                if (!viagem.getPartida().getPontoPartida().equals(origem)) {
                    try {
                        tempoEntreViagem = calcularTempo(viagem.getPartida().getPontoPartida(), origem);
                    } catch (Exception e) {
                        logger.error("Erro ao calcular o tempo entre viagens: {}", e.getMessage());
                    }
                }

                ListagemMotoristaViagem dto = new ListagemMotoristaViagem();
                dto.setIdViagem(viagem.getIdViagem());
                dto.setNomeMotorista(motorista.getNome());
                dto.setInicioViagem(viagem.getHorarioViagem());
                try {
                    dto.setFimViagem(calcularFimViagem(viagem.getHorarioViagem(), dataHoraViagem, origem, destino));
                    dto.setDistanciaPontoPartidaViagem(calcularDistancia(origem, viagem.getPartida().getPontoPartida()));
                    dto.setDistanciaPontoDestinoViagem(calcularDistancia(destino, viagem.getDestino().getPontoDestino()));
                } catch (Exception e) {
                    logger.error("Erro ao calcular o fim da viagem: {}", e.getMessage());
                }
                dto.setValor(viagem.getValor());

                dtos.add(dto);
            }
        }

        return dtos;
    }

    private double calcularDistancia(String origem, String destino) throws Exception {
        OkHttpClient client = new OkHttpClient();
        Logger logger = LoggerFactory.getLogger(getClass());

        String apiKey = "AIzaSyCjfdkq8uHIzU3mr1OZqAtkJggC-k2PZdo";

        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric" +
                "&origins=" + URLEncoder.encode(origem, StandardCharsets.UTF_8.toString()) +
                "&destinations=" + URLEncoder.encode(destino, StandardCharsets.UTF_8.toString()) +
                "&key=" + apiKey;

        logger.debug("URL da requisição: {}", url);

        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            JSONObject jsonObject = new JSONObject(response.body().string());
            String status = jsonObject.getString("status");

            if (!"OK".equals(status)) {
                throw new Exception("Erro na resposta da API do Google: " + status);
            }

            JSONArray rows = jsonObject.getJSONArray("rows");

            if (rows.isEmpty()) {
                throw new Exception("Nenhum resultado encontrado na matriz de distância");
            }

            JSONObject elements = rows.getJSONObject(0).getJSONArray("elements").getJSONObject(0);
            String elementStatus = elements.getString("status");

            if (!"OK".equals(elementStatus)) {
                throw new Exception("Erro ao calcular a distância: " + elementStatus);
            }

            double distancia = elements.getJSONObject("distance").getDouble("value") / 1000.0; // Converter metros para quilômetros
            logger.debug("Distância calculada: {} km", distancia);
            return distancia;
        } catch (IOException e) {
            logger.error("Erro ao realizar a requisição para calcular a distância", e);
            throw new Exception("Erro ao calcular a distância", e);
        }
    }

    private String calcularFimViagem(String inicioViagem, LocalDate diaViagem, String origem, String destino) throws Exception {
        Logger logger = LoggerFactory.getLogger(getClass());
        try {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalDateTime inicio = LocalDateTime.of(diaViagem, LocalTime.parse(inicioViagem, timeFormatter));
            double duracao = calcularTempo(origem, destino);
            LocalDateTime fim = inicio.plusMinutes((long) duracao);
            return fim.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } catch (DateTimeParseException e) {
            logger.error("Erro ao analisar a hora de início da viagem: {}", e.getMessage());
            throw new Exception("Erro ao calcular o fim da viagem", e);
        }
    }

    public double calcularTempo(String origem, String destino) throws Exception {
        OkHttpClient client = new OkHttpClient();
        Logger logger = LoggerFactory.getLogger(getClass());

        String apiKey = "AIzaSyCjfdkq8uHIzU3mr1OZqAtkJggC-k2PZdo";

        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric" +
                "&origins=" + URLEncoder.encode(origem, StandardCharsets.UTF_8.toString()) +
                "&destinations=" + URLEncoder.encode(destino, StandardCharsets.UTF_8.toString()) +
                "&key=" + apiKey;

        logger.debug("URL da requisição: {}", url);

        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            JSONObject jsonObject = new JSONObject(response.body().string());
            JSONArray rows = jsonObject.getJSONArray("rows");

            if (rows.isEmpty()) {
                throw new Exception("No results found for the distance matrix");
            }

            JSONObject elements = rows.getJSONObject(0).getJSONArray("elements").getJSONObject(0);
            if (elements.getString("status").equals("OK")) {
                double duracao = elements.getJSONObject("duration").getDouble("value") / 60; // Converter segundos para minutos
                logger.debug("Duração calculada: {} minutos", duracao);
                return duracao;
            } else {
                throw new Exception("Erro ao calcular a duração: " + elements.getString("status"));
            }
        } catch (Exception e) {
            logger.error("Erro ao realizar a requisição para calcular a duração", e);
            throw new Exception("Erro ao calcular a duração", e);
        }
    }



    public List<Viagem> listarViagensPorPassageiro(Usuario passageiro) {
        return viagemRepository.findByListaPassageirosContains(passageiro);
    }




    public List<Viagem> consultarViagens(LocalDate horaViagem) {
        Logger logger = LoggerFactory.getLogger(getClass());

        logger.debug("Consultando motoristas para a hora de viagem: {}", horaViagem);

        // Verificando se viagemRepository é nulo
        if (viagemRepository == null) {
            logger.error("O viagemRepository não foi injetado corretamente.");
            throw new IllegalStateException("O viagemRepository não foi injetado corretamente.");
        }

        List<Viagem> viagensParaHorario = new ArrayList<>();

        try {
            viagensParaHorario = viagemRepository.findViagensAndUsuariosByHorario(horaViagem);


            if (viagensParaHorario == null || viagensParaHorario.isEmpty()) {
                logger.warn("Nenhuma viagem encontrada para o horário {}", horaViagem);
            } else {
                logger.debug("Encontradas {} viagens para o horário {}", viagensParaHorario.size(), horaViagem);
            }
        } catch (Exception e) {
            logger.error("Erro ao consultar viagens para o horário {}: {}", horaViagem, e.getMessage());

            throw new RuntimeException("Erro ao consultar viagens.", e);
        }

        return viagensParaHorario;
    }


    public ReservaViagemDto reservarViagem(Integer idViagem, Integer idUsuario) {
        Viagem viagem = buscarViagemPorId(idViagem);

        validarMotoristaNaoPodeSerPassageiro(viagem, idUsuario);

        validarViagemDisponivel(viagem);

        Usuario passageiro = buscarUsuarioPorId(idUsuario);

        adicionarPassageiroNaViagem(viagem, passageiro);

        salvarViagem(viagem);

        return criarReservaViagemDto(viagem);
    }

    private void validarViagemDisponivel(Viagem viagem) {
        if (viagem.getStatus() == Viagem.StatusViagem.FINALIZADA) {
            throw new IllegalArgumentException("Não é possível reservar esta viagem pois ela já está finalizada.");
        }
        if (viagem.getQntPassageiros() >= 4) {
            throw new IllegalArgumentException("Não é possível reservar esta viagem pois atingiu o limite máximo de passageiros.");
        }
    }

    private Viagem buscarViagemPorId(Integer idViagem) {
        return viagemRepository.findById(idViagem)
                .orElseThrow(() -> new IllegalArgumentException("Viagem não encontrada para o ID: " + idViagem));
    }

    private void validarMotoristaNaoPodeSerPassageiro(Viagem viagem, Integer idUsuario) {
        if (viagem.getMotorista().getId() == idUsuario) {
            throw new IllegalArgumentException("O motorista não pode se associar à própria viagem como passageiro.");
        }
    }

    private Usuario buscarUsuarioPorId(Integer idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado para o ID: " + idUsuario));
    }

    private void adicionarPassageiroNaViagem(Viagem viagem, Usuario passageiro) {
        if (viagem.getMotorista().getId() == passageiro.getId()) {
            throw new IllegalArgumentException("O motorista não pode se cadastrar na própria viagem como passageiro.");
        }

        if (viagem.getListaPassageiros().contains(passageiro)) {
            throw new IllegalArgumentException("Este usuário já está incluído como passageiro nesta viagem.");
        }

        if (viagem.getQntPassageiros() >= 4) {
            throw new IllegalArgumentException("Não é possível adicionar mais passageiros, a viagem já atingiu o limite máximo.");
        }

        viagem.getListaPassageiros().add(passageiro);
        viagem.setQntPassageiros(viagem.getQntPassageiros() + 1);
    }

    private void salvarViagem(Viagem viagem) {
        viagemRepository.save(viagem);
    }

    private ReservaViagemDto criarReservaViagemDto(Viagem viagem) {
        ReservaViagemDto reservaViagemDto = new ReservaViagemDto();
        reservaViagemDto.setIdViagem(viagem.getIdViagem());
        reservaViagemDto.setValor(viagem.getValor());
        reservaViagemDto.setQntPassageiros(viagem.getQntPassageiros());
        reservaViagemDto.setSoMulheres(viagem.getSoMulheres());
        reservaViagemDto.setTempoMedioViagem(viagem.getTempoMedioViagem());
        reservaViagemDto.setPontoPartida(viagem.getPartida().getPontoPartida());
        reservaViagemDto.setPontoDestino(viagem.getDestino().getPontoDestino());
        reservaViagemDto.setMotorista(viagem.getMotorista().getNome());

        Carro carro = viagem.getCarro();
        if (carro != null) {
            reservaViagemDto.setMarcaCarro(carro.getMarca());
            reservaViagemDto.setModeloCarro(carro.getModelo());
            reservaViagemDto.setPlacaCarro(carro.getPlaca());
        }

        List<String> passageiros = viagem.getListaPassageiros().stream().map(Usuario::getNome).collect(Collectors.toList());
        reservaViagemDto.setPassageiros(passageiros);

        return reservaViagemDto;
    }
}
