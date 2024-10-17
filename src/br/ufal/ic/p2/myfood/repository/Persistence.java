package br.ufal.ic.p2.myfood.repository;

import br.ufal.ic.p2.myfood.exception.persistence.*;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Persistence {
    private static final String DIRECTORY_PATH = "./src/br/ufal/ic/p2/myfood/data";
    private static final String FILE_PERSISTENCE = DIRECTORY_PATH + File.separator + "data.xml";

    public static void saveData(Data data) throws Exception {
        File directory = new File(DIRECTORY_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        eraseData();

        try (XMLEncoder e = new XMLEncoder(
                new BufferedOutputStream(
                        new FileOutputStream(FILE_PERSISTENCE)))) {

            e.writeObject(data);

        } catch (FileNotFoundException e) {
            throw new ErroAoSalvarArquivoException();
        }
    }

    public static Data loadData() throws Exception {
        File arquivo = new File(FILE_PERSISTENCE);
        if (arquivo.exists()) {
            try (XMLDecoder d = new XMLDecoder(
                    new BufferedInputStream(
                            new FileInputStream(FILE_PERSISTENCE)))) {
                return (Data) d.readObject();
            } catch (FileNotFoundException e) {
                throw new ErroAoCarregarArquivoException();
            }
        }
        return null;
    }

    public static void eraseData() throws Exception {
        File arquivo = new File(FILE_PERSISTENCE);
        try {
            if (arquivo.exists()) {
                Files.delete(Path.of(FILE_PERSISTENCE));
            }
        } catch (IOException e) {
            throw new ErroAoApagarArquivoException();
        }
    }
}
