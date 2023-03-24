
public class Wunschlistengenerator {

	
	public static void main(String[] args) {

		
		StringBuilder wunschliste = new StringBuilder("Zu Weihnachten wünsche ich mir ");

		
		for (int geschenkeNr = 1; geschenkeNr <= 3; geschenkeNr++) {

			
			String geschenk = "";

			switch ((int)(5 * Math.random())) {
			case 0:
				geschenk = "Mobiltelefon"; break;
			case 1:
				geschenk = "Pony"; break;
			case 2:
				geschenk = "Snowboard"; break;
			case 3:
				
				geschenk = "Elefanten"; break;
			case 4:
				geschenk = "Weihnachtsbaum";
			}


			
			String eigenschaft = "";

			if (!geschenk.equals("Weihnachtsbaum")) {

				switch ((int)(2 * Math.random())) {
					case 0:

						
						if(!geschenk.equals("Elefanten")) {
							eigenschaft = "schnelles";
						} else {
							eigenschaft = "schnellen";
						}

						break;
					case 1:
						eigenschaft = "rosa";
				}g

			} else {

				
				eigenschaft = "rosa";

			}

			
			String artikel = "";
			switch (geschenk) {
			case "Mobiltelefon":
				artikel = "ein"; break;
			case "Pony":
				artikel = "ein"; break;
			case "Snowboard":
				artikel = "ein"; break;
			case "Weihnachtsbaum":
				artikel = "einen"; break;
			case "Elefanten":
				artikel = "einen";
			}

			
			wunschliste.append(artikel).append(" ").append(eigenschaft).append(" ").append(geschenk);

			
			if (geschenkeNr < 2) {
				wunschliste.append(", ");
			} else if (geschenkeNr == 2) {
				wunschliste.append(" und ");
			}

		}

		wunschliste.append(" ");

	
		System.out.println(wunschliste);

	}

}
