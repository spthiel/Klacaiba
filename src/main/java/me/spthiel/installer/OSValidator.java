package me.spthiel.installer;

public class OSValidator {
	
	static enum OS {
		
		WINDOWS(System.getenv("APPDATA") + "\\.minecraft"),
		MAC(System.getProperty("user.home") + "/.minecraft"),
		UNIX(System.getProperty("user.home") + "/Library/Application Support/minecraft"),
		SOLARIS(System.getProperty("user.home") + "/Library/Application Support/minecraft"),
		UNKNOWN("");
		
		private final String basePath;
		
		OS(String basePath) {
			
			this.basePath = basePath;
		}
		
		public String getBasePath() {
			
			return basePath;
		}
	}
	
	private static final String osName     = System.getProperty("os.name").toLowerCase();
	private static final OS     os;
	
	static {
		
		if (osName.contains("win")) {
			os = OS.WINDOWS;
		} else if(osName.contains("mac")) {
			os = OS.MAC;
		} else if(osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
			os = OS.UNIX;
		} else if (osName.contains("sunos")) {
			os = OS.SOLARIS;
		} else {
			os = OS.UNKNOWN;
		}
		
	}
	
	public static OS getOS() {
		return os;
	}
	
}