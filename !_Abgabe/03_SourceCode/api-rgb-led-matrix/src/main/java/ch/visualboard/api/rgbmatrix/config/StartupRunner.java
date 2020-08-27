package ch.visualboard.api.rgbmatrix.config;

import ch.visualboard.api.rgbmatrix.services.LedMatrixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component("ee0eb71a-ee75-415b-ae39-6b65dbcc58e7")
public class StartupRunner implements CommandLineRunner
{
    private final LedMatrixService ledMatrixService;

    @Autowired
    public StartupRunner(
        final LedMatrixService ledMatrixService
    )
    {
        this.ledMatrixService = ledMatrixService;
    }

    @Override
    public void run(String... args)
    {
        ledMatrixService.showLastDisplayedImage();
    }
}
