package mg.softlab.sirh.util.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
public class File {

    /**
     * Enregistrer un fichier
     * @param file Le fichier à enregistrer
     * @param folder Le dossier où le fichier doit être enregistré
     * @param fileName Le nom du fichier une fois qu'il est enregistré
     * @return Un objet indiquant si le fichier est enregistré ou non
     */
    public static void saveFile(MultipartFile file, String folder, String fileName) throws IOException {

        var uploadPath = Paths.get(folder);
        if (!Files.exists(uploadPath)) {
            String[] folders = folder.split("/");
            for(int i = 0; i < folders.length; i++) {
                if(i == 0) {
                    Path dir = Paths.get(folders[i]);
                    if( !Files.exists(dir) )
                        Files.createDirectory(dir);
                    continue;
                }
                String path = folders[0];
                for(int j = 1; j <= i; j++) {
                    path = path + "/" + folders[j];
                }
                Path dir = Paths.get(path);
                if( !Files.exists(dir) )
                    Files.createDirectory(dir);
            }
        }

        var dest = Paths.get(folder + "/" +fileName);
        Files.copy(file.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);
    }

}
