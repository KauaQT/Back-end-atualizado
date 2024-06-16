package sptech.school.service;

import sptech.school.enity.Usuario;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Formatter;
import java.util.List;

public class GerarCsvService {

    public File gravaArquivoCsv(List<Usuario> lista, String nomeArq) {
        FileWriter arq = null;
        Formatter saida = null;
        Boolean deuRuim = false;

        nomeArq += ".csv";
        File file = new File(nomeArq);

        try {
            arq = new FileWriter(file);
            saida = new Formatter(arq);
        } catch (IOException erro) {
            System.out.println("Erro ao abrir o arquivo");
            erro.printStackTrace();
            System.exit(1);
        }

        try{
            for (Usuario usuario : lista) {
                saida.format("%d;%s;%s;%s;%s\n", usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getSenha(), usuario.getTipoUsuario());
            }
        }
        catch (Exception erro) {
            System.out.println("Erro ao gravar o arquivo");
            erro.printStackTrace();
            deuRuim = true;
        } finally {
            saida.close();
            try {
                arq.close();
            } catch (IOException erro) {
                System.out.println("Erro ao fechar o arquivo");
                deuRuim = true;
            }
            if (deuRuim) {
                System.exit(1);
            }
        }

        return file;
    }
}