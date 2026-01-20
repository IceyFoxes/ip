public class Icey {
    private static final String NAME = "Icey";
    private static final String DIVIDER = "─".repeat(60);

    private void greetUser() {
        System.out.println(DIVIDER);
        System.out.println("Hello! I'm " + NAME + ".");
        System.out.println("What can I do for you?");
    }

    private void exit() {
        System.out.println(DIVIDER);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(DIVIDER);
    }

    public static void main(String[] args) {
        Icey icey = new Icey();
        icey.greetUser();
        icey.exit();
    }
}
