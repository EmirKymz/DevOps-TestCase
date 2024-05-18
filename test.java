import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CreateTestDirectory {
    public static void main(String[] args) {
        // "/home/test" adında bir klasör oluştur
        File testDir = new File("test42");

        if (!testDir.exists()) {
            if (testDir.mkdir()) {
                System.out.println("Test klasörü başarıyla oluşturuldu.");
            } else {
                System.out.println("Test klasörü oluşturulamadı.");
                return;
            }
        }

        // "/home/test/readme.txt" dosyasını oluştur
        File readmeFile = new File(testDir, "readme.txt");

        try (FileWriter writer = new FileWriter(readmeFile)) {
            // "Hello World" yaz
            writer.write("Hello World");
            System.out.println("readme.txt dosyasına 'Hello World' yazıldı.");
        } catch (IOException e) {
            System.out.println("Dosya oluşturma/yazma hatası: " + e.getMessage());
        }
    }
}
