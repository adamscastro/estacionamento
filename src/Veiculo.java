
import java.time.LocalTime;

class Veiculo {
    private LocalTime horaEntrada;
    private LocalTime horaSaida;
    private String placaVeiculo;

    public Veiculo(LocalTime horaEntrada, String placaVeiculo) {
        this.horaEntrada = horaEntrada;
        this.placaVeiculo = placaVeiculo;
    }

    public LocalTime getHoraEntrada() {
        return horaEntrada;
    }

    public String getPlacaVeiculo() {
        return placaVeiculo;
    }

    public void setHoraSaida(LocalTime horaSaida) {
        this.horaSaida = horaSaida;
    }

    public LocalTime getHoraSaida() {
        return horaSaida;
    }
}
