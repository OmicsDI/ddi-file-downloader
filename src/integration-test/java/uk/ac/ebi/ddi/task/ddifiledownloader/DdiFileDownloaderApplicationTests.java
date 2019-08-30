package uk.ac.ebi.ddi.task.ddifiledownloader;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.ddi.ddifileservice.services.IFileSystem;
import uk.ac.ebi.ddi.task.ddifiledownloader.configuration.FileDownloaderTaskProperties;

import java.io.File;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DdiFileDownloaderApplication.class,
		initializers = ConfigFileApplicationContextInitializer.class)
@TestPropertySource(properties = {
		"downloader.originalFileURL=https://www.metabolome-express.org/public/dataset_index.xml",
		"downloader.targetFileName=/tmp/metabolome/dataset_index.xml",
		"file.provider=local"
})
public class DdiFileDownloaderApplicationTests {

	@Autowired
	private FileDownloaderTaskProperties taskProperties;

	@Autowired
	private IFileSystem fileSystem;

	@Autowired
	private DdiFileDownloaderApplication application;

	@Before
	public void setUp() throws Exception {
		new File(taskProperties.getTargetFileName()).getParentFile().mkdirs();
	}

	@Test
	public void contextLoads() throws Exception {
		application.run();
		Assert.assertTrue(new File(taskProperties.getTargetFileName()).exists());
	}
}
