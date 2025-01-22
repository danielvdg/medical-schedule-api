package br.com.codart.src.domain.entities.schedule;

import br.com.codart.src.domain.Enum.ScheduleStatusEnum;
import br.com.codart.src.domain.Enum.SlotStatusEnum;
import br.com.codart.src.domain.entities.Slot;
import br.com.codart.src.service.message.ResourceMessage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Schedule {

    private Long scheduleId ;
    private LocalDateTime scheduleDate;
    private ScheduleStatusEnum status;
    private List<Slot> times = new ArrayList<>();

    // Construtores
    public Schedule(Long scheduleId,LocalDateTime scheduleDate, ScheduleStatusEnum status, List<Slot> times) {
        this.scheduleId = scheduleId;
        this.scheduleDate = scheduleDate;
        this.status = status;
        this.times = times;

        validateConstructor(this);
    }

    void validateConstructor(Schedule schedule) {
        if(schedule.scheduleId == null || schedule.scheduleDate == null || schedule.status == null || schedule.times == null) {
            throw new IllegalArgumentException(ResourceMessage.SCHEDULE_INVALID.getMessage());
        }
        if (schedule.scheduleId <= 0) {
            throw new IllegalArgumentException(ResourceMessage.SCHEDULE_INVALID.getMessage());
        }
        if (schedule.scheduleDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(ResourceMessage.SCHEDULE_INVALID.getMessage());
        }
        if (schedule.times.isEmpty()) {
            throw new IllegalArgumentException(ResourceMessage.SCHEDULE_INVALID.getMessage());
        }
    }

    // Getters and Setters

    public Long getId() {
        return scheduleId;
    }

    public LocalDateTime getScheduleDate() {
        return scheduleDate;
    }

    public ScheduleStatusEnum getStatus() {
        return status;
    }

    public List<Slot> getTimes() {
        return times;
    }

    public void setTimes(List<Slot> times) {
        this.times = times;
    }

    // Metodos de criação ou factory

    public Schedule createSchedule(Long id, LocalDateTime scheduleDate, List<Slot> times) {
        if (scheduleDate == null) {
            throw new IllegalArgumentException("A data do agendamento nao pode ser nula.");
        }

        return new Schedule(id, scheduleDate, ScheduleStatusEnum.ACTIVE, times);
    }

    public Schedule blockSchedule(Long id, LocalDateTime scheduleDate, List<Slot> times) {
        if (scheduleDate == null) {
            throw new IllegalArgumentException("A data do agendamento nao pode ser nula.");
        }

        return new Schedule(id, scheduleDate, ScheduleStatusEnum.BLOCKED, times);
    }

    public Schedule cancelSchedule(Long id, LocalDateTime scheduleDate, List<Slot> times) {
        if (scheduleDate == null) {
            throw new IllegalArgumentException("A data do agendamento nao pode ser nula.");
        }
        return new Schedule(id, scheduleDate, ScheduleStatusEnum.CANCELLED, times);
    }

    //validações de Slot

    public void validateSlot(Slot slot) {
        if (slot.getSlotStatus() == SlotStatusEnum.BLOCKED) {
            throw new IllegalArgumentException("O horário selecionado esta bloqueado.");
        }

        if (slot.getSlotStatus() == SlotStatusEnum.RESERVED) {
            throw new IllegalArgumentException("O horário selecionado ja foi reservado.");
        }

    }

    private void validateSlots(List<Slot> slots) {
        for (Slot slot : slots) {
            validateSlot(slot);
        }
    }

    // Cálculo automático do estado da agenda com base nos slots
    private ScheduleStatusEnum calculateScheduleStatus(List<Slot> slots) {
        if (slots == null || slots.isEmpty()) {
            return ScheduleStatusEnum.DEFAULT; // Caso não existam slots, a agenda é considerada CANCELLED
        }

        boolean hasActive = false;
        boolean allBlocked = true;

        for (Slot slot : slots) {
            if (slot.getSlotStatus() == SlotStatusEnum.AVAILABLE) {
                hasActive = true;
            }
            if (slot.getSlotStatus() != SlotStatusEnum.BLOCKED) {
                allBlocked = false;
            }
        }

        if (allBlocked) {
            return ScheduleStatusEnum.BLOCKED;
        } else if (hasActive) {
            return ScheduleStatusEnum.ACTIVE;
        } else {
            return ScheduleStatusEnum.CANCELLED;
        }
    }

}
