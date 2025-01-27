package br.com.codart.src.domain.entities.schedule;

import br.com.codart.src.domain.Enum.ScheduleStatusEnum;
import br.com.codart.src.domain.Enum.SlotStatusEnum;
import br.com.codart.src.domain.entities.Slot;
import br.com.codart.src.service.message.ResourceMessage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Schedule {

    private final Long scheduleId ;
    private final LocalDateTime scheduleDate;
    private final ScheduleStatusEnum status;
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

        if(schedule.scheduleId == null || schedule.scheduleDate == null || schedule.status == null ) {
            throw new IllegalArgumentException(ResourceMessage.SCHEDULE_INVALID.getMessage());
        }

        if (schedule.scheduleId <= 0) {
            throw new IllegalArgumentException(ResourceMessage.SCHEDULE_INVALID.getMessage());
        }

        validateSlots(schedule.times);
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


    // metodos de comportamento da classe
    // Metodos de criação ou factory

    public static Schedule createSchedule(Long id, LocalDateTime scheduleDate, List<Slot> times) {
        if (scheduleDate == null) {
            throw new IllegalArgumentException(ResourceMessage.SCHEDULE_NOT_NULL.getMessage());
        }
        return new Schedule(id, scheduleDate, ScheduleStatusEnum.ACTIVE, times);
    }

    public static Schedule blockSchedule(Long id, LocalDateTime scheduleDate, List<Slot> times) {
        if (scheduleDate == null) {
            throw new IllegalArgumentException(ResourceMessage.SCHEDULE_NOT_NULL.getMessage());
        }

        return new Schedule(id, scheduleDate, ScheduleStatusEnum.BLOCKED, times);
    }

    public static Schedule cancelSchedule(Long id, LocalDateTime scheduleDate, List<Slot> times) {
        if (scheduleDate == null) {
            throw new IllegalArgumentException(ResourceMessage.SCHEDULE_NOT_NULL.getMessage());
        }
        return new Schedule(id, scheduleDate, ScheduleStatusEnum.CANCELLED, times);
    }

    //validações de Slot
    public void validateSlot(Slot slot) {
        if (slot.getSlotStatus() == SlotStatusEnum.BLOCKED) {
            throw new IllegalArgumentException(ResourceMessage.SCHEDULE_BLOCKED.getMessage());
        }

        if (slot.getSlotStatus() == SlotStatusEnum.RESERVED) {
            throw new IllegalArgumentException(ResourceMessage.SCHEDULE_RESERVED.getMessage());
        }
    }

    private void validateSlots(List<Slot> slots) {

        if (slots == null || slots.isEmpty()) {
            throw new IllegalArgumentException(ResourceMessage.SCHEDULE_INVALID.getMessage());
        }

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

    public static class Builder {

        private Long scheduleId;
        private LocalDateTime scheduleDate;
        private ScheduleStatusEnum status;
        private List<Slot> times = new ArrayList<>();

        public Builder scheduleId(Long scheduleId) {
            this.scheduleId = scheduleId;
            return this;
        }

        public Builder scheduleDate(LocalDateTime scheduleDate) {
            this.scheduleDate = scheduleDate;
            return this;
        }

        public Builder status(ScheduleStatusEnum status) {
            this.status = status;
            return this;
        }

        public Builder times(List<Slot> times) {
            this.times = times;
            return this;
        }

        public Schedule build() {
            return new Schedule(scheduleId, scheduleDate, status, times);
        }
    }

}
