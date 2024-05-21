import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class test {
    //bu kodu çalıştırmadan önce test42 adında bir klasör oluşturur ve içine readme.txt adında bir dosya oluşturur.
    //dosyaya "Hello World" yazar.
    public static void main(String[] args) {
        File testDir = new File("test42");

        if (!testDir.exists()) {
            if (testDir.mkdir()) {
                System.out.println("Test klasörü başariyla oluşturuldu.");
            } else {
                System.out.println("Test klasörü oluşturulamadi.");
                return;
            }
        }

        File readmeFile = new File(testDir, "readme.txt");

        try (FileWriter writer = new FileWriter(readmeFile)) {
            writer.write("Hello World");
            System.out.println("readme.txt dosyasina 'Hello World' yazildi.");
        } catch (IOException e) {
            System.out.println("Dosya oluşturma/yazma hatasi: " + e.getMessage());
        }
    }
}
