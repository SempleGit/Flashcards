package flashcards;

public class Main {
    public static void main(String[] args) {
        FlashCard flashCard = new FlashCard();
        String action = "";

        if (args.length > 0) {
            for (int i = 0; i < args.length - 1; i += 2) {
                if ("-import".equals(args[i])) {
                    flashCard.importFile = args[i + 1];
                    flashCard.importSet = true;
                    flashCard.importCard();
                } else if ("-export".equals(args[i])) {
                    flashCard.exportFile = args[i + 1];
                    flashCard.exportSet = true;
                }
            }
        }

        while (!"exit".equals(action)) {
            flashCard.output("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):\n");
            action = flashCard.getInput().toLowerCase();

            switch (action) {
                case "add":
                    flashCard.addCard();
                    break;
                case "remove":
                    flashCard.removeCard();
                    break;
                case "import":
                    flashCard.importSet = false;
                    flashCard.importCard();
                    break;
                case "export":
                    flashCard.exportCard();
                    break;
                case "ask":
                    flashCard.askCard();
                    break;
                case "exit":
                    flashCard.exit();
                    break;
                case "log":
                    flashCard.log();
                    break;
                case "hardest card":
                    flashCard.hardestCard();
                    break;
                case "reset stats":
                    flashCard.resetStats();
                    break;
            }
        }
    }
}
