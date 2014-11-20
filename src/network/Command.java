package network;

/**
 * 
 * @author Axel Sigl
 *
 */
public class Command {

	/**
	 * 
	 * @author Axel Sigl
	 * @param input
	 * @return
	 */
	public String parseUserCommand(String input) {
		String cmd[] = new String[100];

		cmd = input.split("\\s");

		switch (cmd[0]) {
		
		case("help"):
			return "Basic syntax: COMMAND [OPTIONS] [ARGUMENTS]";
		}
		
		return "Unknown command " + cmd[0];
	}

	/**
	 * 
	 * @author Axel Sigl
	 * @param input
	 * @return
	 */
	public String parseRootCommand(String input) {
		String cmd[] = new String[3];

		cmd = input.split("\\s");

		switch (cmd[0]) {

		case ("quit"):
			Server.println("Server shutting down");
			System.exit(0);
			
		case("help"):
			return "Basic syntax: COMMAND [OPTIONS] [ARGUMENTS]";
		}
		
		return "Unknown command " + cmd[0];
	}
}
