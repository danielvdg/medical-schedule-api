package br.com.codart.src.service.message;

public enum ResourceMessage {
    SLOT_EXPIRED("O slot expirou."),
    SLOT_BLOCKED ("O slot foi bloqueado."),
    NOT_AVAILABLE("O slot solicitado nao esta disponivel."),
    NOT_RESERVED("O slot solicitado nao esta reservado."),
    SLOT_INVALID("O slot selecionado é inválido."),
    SCHEDULE_INVALID("O horário selecionado é inválido.");

    private final String message;

    ResourceMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String format(String... args) {
        return String.format(message, (Object[]) args);
    }
}
