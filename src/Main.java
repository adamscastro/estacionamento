import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);

        Estacionamento estacionamento = new Estacionamento();

        int opcao;
        do {
            exibirMenu();
            opcao = entrada.nextInt();

            switch (opcao) {
                case 1:
                    estacionamento.inserirVeiculo();
                    break;
                case 2:
                    estacionamento.saidaVeiculo();
                    break;
                case 3:
                    estacionamento.exibirInformacoes();
                    break;
                case 4:
                    System.out.println("Saindo do programa. Até logo!");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 4);
    }

    private static void exibirMenu() {
        System.out.println("\nEscolha uma opção: ");
        System.out.println("1 - Entrada de veículo");
        System.out.println("2 - Saída de veículo");
        System.out.println("3 - Informações");
        System.out.println("4 - Sair");
    }
}