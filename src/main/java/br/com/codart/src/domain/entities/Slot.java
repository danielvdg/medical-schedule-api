package br.com.codart.src.domain.entities;

import br.com.codart.src.domain.Enum.SlotStatusEnum;
import br.com.codart.src.service.message.ResourceMessage;

import java.time.LocalDateTime;

public class Slot {

    private Long slotId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private SlotStatusEnum slotStatus;

    public Slot(Long slotId, LocalDateTime startTime, LocalDateTime endTime, SlotStatusEnum slotStatus) {
        this.slotId = slotId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.slotStatus = slotStatus;

        validateConstructor(this);
    }

    public void validateConstructor(Slot slot) {
        if(slot.slotId == null || slot.startTime == null || slot.endTime == null || slot.slotStatus == null) {
            throw new IllegalArgumentException(ResourceMessage.SLOT_INVALID.getMessage());
        }

        if(slot.slotId <= 0) {
            throw new IllegalArgumentException(ResourceMessage.SLOT_INVALID.getMessage());
        }

        if (slot.startTime.isAfter(slot.endTime)) {
            throw new IllegalArgumentException(ResourceMessage.SLOT_INVALID.getMessage());
        }

        if(slot.startTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(ResourceMessage.SLOT_INVALID.getMessage());
        }

        if (slot.endTime.isBefore(slot.startTime)) {
            throw new IllegalArgumentException(ResourceMessage.SLOT_INVALID.getMessage());
        }

        if (slot.endTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(ResourceMessage.SLOT_INVALID.getMessage());
        }

    }

    public Long getSlotId() {
        return slotId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public SlotStatusEnum getSlotStatus() {
        return slotStatus;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    // Métodos de Validação
    private void validateNotExpired() {
        if (LocalDateTime.now().isAfter(endTime)) {
            throw new IllegalStateException(ResourceMessage.SLOT_EXPIRED.getMessage());
        }
    }

    private void validateStateForBlocking() {
        if (slotStatus == SlotStatusEnum.RESERVED || slotStatus == SlotStatusEnum.CANCELLED || slotStatus == SlotStatusEnum.RESCHEDULED) {
            throw new IllegalStateException(ResourceMessage.SLOT_BLOCKED.getMessage());
        }
    }

    private void validateStateForCancellation() {
        if (slotStatus == SlotStatusEnum.BLOCKED || slotStatus == SlotStatusEnum.AVAILABLE) {
            throw new IllegalStateException(ResourceMessage.SLOT_BLOCKED.getMessage());
        }
    }

    private void validateStateForReservation() {
        if (slotStatus != SlotStatusEnum.AVAILABLE) {
            throw new IllegalStateException( ResourceMessage.NOT_AVAILABLE.getMessage());
        }
    }

    private void validateStateForReschedule() {
        if (slotStatus != SlotStatusEnum.RESERVED) {
            throw new IllegalStateException(ResourceMessage.NOT_RESERVED.getMessage());
        }
    }

    private void validateStateForReopening() {
        if (slotStatus != SlotStatusEnum.CANCELLED && slotStatus != SlotStatusEnum.BLOCKED) {
            throw new IllegalStateException(ResourceMessage.SLOT_BLOCKED.getMessage());
        }
    }

    // Métodos de Ação

    /**
     * Bloquear um Slot
     * Um slot não pode ser bloqueado se estiver em um dos seguintes estados:
     * RESERVED: Já reservado para um paciente.
     * CANCELLED: Cancelado.
     * RESCHEDULED: Já reagendado.
     */
    public void blockSlot() {
        validateNotExpired();
        validateStateForBlocking();
        this.slotStatus = SlotStatusEnum.BLOCKED;
    }

    /**
     * Cancelar um Slot
     * Um slot não pode ser cancelado se estiver em um dos seguintes estados:
     * BLOCKED: Já bloqueado.
     * AVAILABLE: Livre para agendamento.
     */
    public void cancelSlot() {
        validateNotExpired();
        validateStateForCancellation();
        this.slotStatus = SlotStatusEnum.CANCELLED;
    }

    /**
     *Reservar um Slot
     * Um slot só pode ser reservado se estiver no estado AVAILABLE.
     */
    public void reserveSlot() {
        validateNotExpired();
        validateStateForReservation();
        this.slotStatus = SlotStatusEnum.RESERVED;
    }

    /**
     *Reagendar um Slot
     * Apenas slots no estado RESERVED podem ser reagendados.
     */
    public void rescheduleSlot(LocalDateTime newStartTime, LocalDateTime newEndTime, int durationMinutes) {
        validateNotExpired();
        validateStateForReschedule();
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException(ResourceMessage.NOT_AVAILABLE.getMessage());
        }
        this.startTime = newStartTime;
        this.endTime = newEndTime;
        this.slotStatus = SlotStatusEnum.RESCHEDULED;

    }

    /**
     *Reabrir um Slot
     * Apenas slots nos estados CANCELLED ou BLOCKED podem ser reabertos, desde que não estejam expirados.
     */
    public void reopenSlot() {
        validateNotExpired();
        validateStateForReopening();
        this.slotStatus = SlotStatusEnum.AVAILABLE;
    }

    /**
     * To String method para
     * serve para mostrar valores dos parametros
     */
    @Override
    public String toString() {
        return "Slot{" +
                "slotId=" + slotId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", slotStatus=" + slotStatus +
                '}';
    }


}
