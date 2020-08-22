package ch.visualboard.api.rgbmatrix.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.restassured.internal.util.IOUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.ResourceUtils;

class FileUtilsTest
{
    @Test
    void testIf_convertMultipartFileToFile_isSuccessful() throws IOException
    {
        File file = ResourceUtils.getFile("classpath:test-image.png");
        String originalName = file.getName();
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] content = IOUtils.toByteArray(fileInputStream);
        MockMultipartFile mockMultipartFile = new MockMultipartFile("Test Image", originalName, null, content);

        File result = FileUtils.convertMultipartFileToFile(mockMultipartFile);

        assertNotNull(result);
        assertEquals(file.getName(), result.getName());
        assertEquals(file.getTotalSpace(), result.getTotalSpace());
        assertEquals(returnFilesContentAsString(file.toPath()), returnFilesContentAsString(result.toPath()));
    }

    private String returnFilesContentAsString(Path path) throws IOException
    {
        byte[] encoded = Files.readAllBytes(path);
        return new String(encoded);
    }
}
