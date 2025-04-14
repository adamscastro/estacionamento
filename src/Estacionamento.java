import java.util.Scanner;
import java.util.Random;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.time.Duration;

class Estacionamento {
    private Veiculo[][] matriz;
    private Random random;
    private Map<String, Veiculo> historicoVeiculos;  // Armazenar histórico de veículos pelo número da placa

    public Estacionamento() {
        matriz = new Veiculo[4][4];
        random = new Random();
        historicoVeiculos = new HashMap<>();

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                matriz[i][j] = null; // Inicializa todas as posições como nulas (sem veículo)
            }
        }

        exibirMatriz();
    }

    public void inserirVeiculo() {
        int linha = random.nextInt(4);
        int coluna = random.nextInt(4);

        if (matriz[linha][coluna] == null) {
            Scanner entrada = new Scanner(System.in);

            System.out.println("Informe o horário de entrada do veículo (HH:mm): ");
            String horaEntradaString = entrada.next();
            LocalTime horaEntrada = LocalTime.parse(horaEntradaString, DateTimeFormatter.ofPattern("HH:mm"));

            String placaVeiculo;
            do {
                System.out.println("Informe a placa do veículo (no formato AAA1A11): ");
                placaVeiculo = entrada.next();
                if (!placaValida(placaVeiculo)) {
                    System.out.println("Placa inválida. Tente novamente.");
                }
            } while (!placaValida(placaVeiculo));

            String estado = validarPlaca(placaVeiculo);
            if (!estado.equals("Placa inválida")) {
                System.out.println("Veículo registrado no estado de " + estado);
            } else {
                System.out.println("Placa inválida. Tente novamente.");
                return;
            }

            Veiculo veiculo = new Veiculo(horaEntrada, placaVeiculo);
            matriz[linha][coluna] = veiculo;

            // Adiciona ao histórico de veículos
            historicoVeiculos.put(placaVeiculo, veiculo);

            System.out.println("Veículo inserido com sucesso na posição (" + linha + ", " + coluna + ")");
        } else {
            System.out.println("Veículo já está estacionado nessa posição. Tentando novamente...");
            inserirVeiculo();
        }

        exibirMatriz();
    }

    public void saidaVeiculo() {
        Scanner entrada = new Scanner(System.in);

        System.out.println("Informe a placa do veículo para a saída: ");
        String placa = entrada.next();

        boolean encontrado = false;

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                if (matriz[i][j] != null && matriz[i][j].getPlacaVeiculo().equalsIgnoreCase(placa)) {
                    System.out.println("Informe o horário de saída do veículo (HH:mm): ");
                    String horaSaidaString = entrada.next();

                    // Converte a string do horário para o tipo LocalTime
                    LocalTime horaSaida = LocalTime.parse(horaSaidaString, DateTimeFormatter.ofPattern("HH:mm"));

                    matriz[i][j].setHoraSaida(horaSaida);

                    // Adiciona ao histórico
                    historicoVeiculos.put(placa, matriz[i][j]);

                    // Calcula o valor do estacionamento
                    double valor = calcularValorEstacionamento(matriz[i][j]);
                    System.out.println("Valor a ser cobrado: R$" + valor);

                    // Calcula o tempo total de permanência
                    Duration duracao = Duration.between(matriz[i][j].getHoraEntrada(), matriz[i][j].getHoraSaida());
                    long minutosTotal = duracao.toMinutes();
                    long horasTotal = duracao.toHoursPart();

                    System.out.println("Tempo total de permanência: " + minutosTotal + " minutos (" + horasTotal + " horas)");

                    imprimirTicket(matriz[i][j], valor);

                    matriz[i][j] = null; // Remove o veículo da matriz
                    System.out.println("Veículo removido com sucesso.");
                    encontrado = true;
                    break;
                }
            }
            if (encontrado) {
                break;
            }
        }

        if (!encontrado) {
            System.out.println("Veículo não encontrado com a placa informada.");
        }

        exibirMatriz();
    }

    private double calcularValorEstacionamento(Veiculo veiculo) {
        Duration duracao = Duration.between(veiculo.getHoraEntrada(), veiculo.getHoraSaida());
        double minutosEstacionado = duracao.toMinutes();

        //Tolerância de 15 minutos
        if (minutosEstacionado <= 15) {
            return 0;
        }

        double valor = 10.0;

        // Valor adicional de R$2 por hora ou fração
        if (minutosEstacionado > 180) {
            valor = valor + Math.ceil((minutosEstacionado - 180) / 60) * 2;
        }

        return valor;
    }


    public void exibirInformacoes() {
        Scanner entrada = new Scanner(System.in);

        int opcao;
        do {
            System.out.println("\nEscolha uma opção: ");
            System.out.println("1 - Detalhes do veículo");
            System.out.println("2 - Histórico");
            System.out.println("3 - Voltar ao menu principal");
            opcao = entrada.nextInt();

            switch (opcao) {
                case 1:
                    detalhesVeiculo();
                    break;
                case 2:
                    exibirHistorico();
                    break;
                case 3:
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcao != 3);
    }

    private void detalhesVeiculo() {
        Scanner entrada = new Scanner(System.in);

        System.out.println("Informe a placa do veículo para detalhes: ");
        String placa = entrada.next();

        Veiculo veiculo = historicoVeiculos.get(placa);

        if (veiculo != null) {
            System.out.println("Horário de entrada: " + veiculo.getHoraEntrada());
            System.out.println("Horário de saída: " + veiculo.getHoraSaida());
            System.out.println("Estado: " + validarPlaca(placa));
            System.out.println("Valor pago: R$" + calcularValorEstacionamento(veiculo));
        } else {
            System.out.println("Veículo não encontrado com a placa informada.");
        }
    }

    private void exibirHistorico() {
        System.out.println("\nHistórico de Veículos:");

        for (Map.Entry<String, Veiculo> entry : historicoVeiculos.entrySet()) {
            Veiculo veiculo = entry.getValue();
            System.out.println("Placa: " + veiculo.getPlacaVeiculo());
            System.out.println("Horário de entrada: " + veiculo.getHoraEntrada());
            System.out.println("Horário de saída: " + veiculo.getHoraSaida());
            System.out.println("Estado: " + validarPlaca(veiculo.getPlacaVeiculo()));
            System.out.println("Valor pago: R$" + calcularValorEstacionamento(veiculo));
            System.out.println("--------------");
        }
    }

    public void imprimirTicket(Veiculo veiculo, double valorEstacionamento) {
        System.out.println("\n-----------------------------------------");
        System.out.println("|             Ticket de Saída            |");
        System.out.println("-----------------------------------------");
        System.out.println("| Placa: " + veiculo.getPlacaVeiculo());
        System.out.println("| Estado: " + validarPlaca(veiculo.getPlacaVeiculo()));
        System.out.println("| Horário de Entrada: " + veiculo.getHoraEntrada());
        System.out.println("| Horário de Saída: " + veiculo.getHoraSaida());

        Duration duracao = Duration.between(veiculo.getHoraEntrada(), veiculo.getHoraSaida());
        long minutosTotal = duracao.toMinutes();
        long horasTotal = duracao.toHoursPart();
        System.out.println("| Tempo de Permanência: " + minutosTotal + " minutos (" + horasTotal + " horas)");

        System.out.println("| Valor do Estacionamento: R$" + valorEstacionamento);
        System.out.println("-----------------------------------------");
    }


    private void exibirMatriz() {
        for (int i = 0; i < matriz.length; i++) {
            System.out.println();
            for (int j = 0; j < matriz[i].length; j++) {
                if (matriz[i][j] != null) {
                    System.out.print("1 " + matriz[i][j].getHoraEntrada() + " | ");
                } else {
                    System.out.print("0 - | ");
                }
            }
        }
        System.out.println();
    }

    private boolean placaValida(String placaVeiculo) {
        // Padrão da placa: AAA1A11 (letra, letra, letra, número, letra, número, número)
        String padrao = "^[A-Z]{3}\\d[A-Z]\\d{2}$";
        return placaVeiculo.matches(padrao);
    }

    // Método para validar a placa e determinar o estado
    private String validarPlaca(String placa) {
        String padrao = "^[A-Z]{3}\\d[A-Z]\\d{2}$";

        if (placa.matches(padrao)) {
            String estado = placa.substring(0, 3);

            if (estado.compareTo("AAA") >= 0 && estado.compareTo("BEZ") <= 0) {
                return "Paraná";
            } else if (estado.compareTo("IAQ") >= 0 && estado.compareTo("JDO") <= 0) {
                return "Rio Grande do Sul";
            } else if (estado.compareTo("LWR") >= 0 && estado.compareTo("MMM") <= 0) {
                return "Santa Catarina";
            } else {
                return "Outro estado";
            }
        } else {
            return "Placa inválida";
        }
    }
}