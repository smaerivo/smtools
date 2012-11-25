import org.apache.log4j.*;

public class ScrapBook
{
	// access point to the Log4j logging facility
	private static final Logger kLogger = Logger.getLogger(ScrapBook.class.getName());

	/*************************
	 * STATIC INITIALISATION *
	 *************************/

	static {
		PropertyConfigurator.configure("log4j.properties");
	}

	/****************
	 * CONSTRUCTORS *
	 ****************/

	public ScrapBook()
	{
		kLogger.info("ScrapBook::ctor()");
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * The application's entry point.
	 */
	public static void main(String[] args)
	{
		ScrapBook scrapBook = new ScrapBook();
	}

	/*******************
	 * PRIVATE METHODS *
	 *******************/
}
