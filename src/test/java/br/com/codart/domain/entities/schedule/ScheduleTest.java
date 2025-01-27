package br.com.codart.domain.entities.schedule;


import br.com.codart.src.domain.Enum.ScheduleStatusEnum;
import br.com.codart.src.domain.Enum.SlotStatusEnum;
import br.com.codart.src.domain.entities.Slot;
import br.com.codart.src.domain.entities.schedule.Schedule;
import br.com.codart.src.service.message.ResourceMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleTest {

    @Nested
    @DisplayName("Teste de comportamento da classe")
    class ConstructorTest {

        @Test
        @DisplayName("Teste de construtor da classe criação com sucesso")
        void testConstructor() {
            Long scheduleId = 1L;
            LocalDateTime scheduleDate = LocalDateTime.now();
            List<Slot> times = new ArrayList<>();

            times.add(Slot.createSlot(1L, LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3), SlotStatusEnum.AVAILABLE));
            times.add(Slot.createSlot(2L, LocalDateTime.now().plusHours(3), LocalDateTime.now().plusHours(4), SlotStatusEnum.AVAILABLE));

            Schedule schedule = Schedule.createSchedule(scheduleId, scheduleDate, times);

            assertNotNull(schedule);
        }

        @Test
        @DisplayName("Deve lançar exceção se o scheduleId for nulo")
        void shouldThrowExceptionIfScheduleIdIsNull() {

            List<Slot> times = new ArrayList<>();
            LocalDateTime scheduleDate = LocalDateTime.now();

            times.add(Slot.createSlot(1L, LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3), SlotStatusEnum.AVAILABLE));

            Exception exception = assertThrows(IllegalArgumentException.class, () ->
                Schedule.createSchedule(null,scheduleDate , times)
            );

            assertNotNull(exception);
            assertEquals(ResourceMessage.SCHEDULE_INVALID.getMessage(), exception.getMessage());

        }

        @Test
        @DisplayName("Deve lançar exceção se o scheduleDate for nulo")
        void shouldThrowExceptionIfScheduleDateIsNull() {

            Long scheduleId = 1L;
            List<Slot> times = new ArrayList<>();

            times.add(Slot.createSlot(1L, LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3), SlotStatusEnum.AVAILABLE));

            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                Schedule.createSchedule(scheduleId, null, times);
            });

            assertNotNull(exception);
            assertEquals(ResourceMessage.SCHEDULE_NOT_NULL.getMessage(), exception.getMessage());
        }

        @Test
        @DisplayName("Deve Lancar exceção se o times for nulo")
        void shouldThrowExceptionIfTimesIsNull() {

            Long scheduleId = 1L;
            LocalDateTime scheduleDate = LocalDateTime.now();

            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                Schedule.createSchedule(scheduleId, scheduleDate, null);
            });

            assertNotNull(exception);
            assertEquals(ResourceMessage.SCHEDULE_INVALID.getMessage(), exception.getMessage());
        }

        @Test
        @DisplayName("Deve Lancar exceção se o times for vazio")
        void shouldThrowExceptionIfTimesIsEmpty() {

            Long scheduleId = 1L;
            LocalDateTime scheduleDate = LocalDateTime.now();
            List<Slot> times = new ArrayList<>();

            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                Schedule.createSchedule(scheduleId, scheduleDate, times);
            });

            assertNotNull(exception);
        }

        @Test
        @DisplayName("Deve lança exceção se o identificador do agendamento for negativo")
        void shouldThrowExceptionIfScheduleIdIsNegative() {

            Long scheduleId = -1L;
            LocalDateTime scheduleDate = LocalDateTime.now();
            List<Slot> times = new ArrayList<>();

            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                Schedule.createSchedule(scheduleId, scheduleDate, times);
            });

            assertNotNull(exception);
            assertEquals(ResourceMessage.SCHEDULE_INVALID.getMessage(), exception.getMessage());
        }

        @Test
        @DisplayName("Deve lança exceção se o status do agendamento for zero")
        void shouldThrowExceptionIfScheduleStatusIsZero() {

            Long scheduleId = 0L;
            LocalDateTime scheduleDate = LocalDateTime.now();
            List<Slot> times = new ArrayList<>();

            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                Schedule.createSchedule(scheduleId, scheduleDate, times);
            });

            assertNotNull(exception);
            assertEquals(ResourceMessage.SCHEDULE_INVALID.getMessage(), exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção se slots for vazio")
        void shouldThrowExceptionIfSlotsIsEmpty() {

            Long scheduleId = 1L;
            LocalDateTime scheduleDate = LocalDateTime.now();
            List<Slot> times = new ArrayList<>();

            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                Schedule.createSchedule(scheduleId, scheduleDate, times);
            });

            assertNotNull(exception);
            assertEquals(ResourceMessage.SCHEDULE_INVALID.getMessage(), exception.getMessage());
        }

    }

    @Nested
    @DisplayName("Teste de comportamento gets da classe")
    class AttributesTest {

        @Test
        @DisplayName("Deve retornar o scheduleId")
        void shouldReturnScheduleId() {
            Long scheduleId = 1L;
            LocalDateTime scheduleDate = LocalDateTime.now();
            List<Slot> times = new ArrayList<>();

            times.add(Slot.createSlot(1L, LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3), SlotStatusEnum.AVAILABLE));

            Schedule schedule = Schedule.createSchedule(scheduleId, scheduleDate, times);
            assertEquals(scheduleId, schedule.getId());
        }

        @Test
        @DisplayName("Deve retornar o scheduleDate")
        void shouldReturnScheduleDate() {
            Long scheduleId = 1L;
            LocalDateTime scheduleDate = LocalDateTime.now();
            List<Slot> times = new ArrayList<>();

            times.add(Slot.createSlot(1L, LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3), SlotStatusEnum.AVAILABLE));

            Schedule schedule = Schedule.createSchedule(scheduleId, scheduleDate, times);

            assertEquals(scheduleDate, schedule.getScheduleDate());
        }

        @Test
        @DisplayName("Deve retornar o status")
        void shouldReturnStatus() {
            Long scheduleId = 1L;
            LocalDateTime scheduleDate = LocalDateTime.now();
            List<Slot> times = new ArrayList<>();

            times.add(Slot.createSlot(1L, LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3), SlotStatusEnum.AVAILABLE));

            Schedule schedule = Schedule.createSchedule(scheduleId, scheduleDate, times);

            assertEquals(ScheduleStatusEnum.ACTIVE, schedule.getStatus());

        }

        @Test
        @DisplayName("Deve retornar os times")
        void shouldReturnTimes() {
            Long scheduleId = 1L;
            LocalDateTime scheduleDate = LocalDateTime.now();
            List<Slot> times = new ArrayList<>();

            times.add(Slot.createSlot(1L, LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3), SlotStatusEnum.AVAILABLE));

            Schedule schedule = Schedule.createSchedule(scheduleId, scheduleDate, times);

            assertEquals(times, schedule.getTimes());
        }
    }

    @Nested
    @DisplayName("Teste de comportamento da classe")
    class BlockScheduleTest {

        @Test
        @DisplayName("Deve Cria um agendamento bloqueado com sucesso")
        void shouldCreateBlockedSchedule() {
            Long scheduleId = 1L;
            LocalDateTime scheduleDate = LocalDateTime.now();
            List<Slot> times = new ArrayList<>();

            times.add(Slot.createSlot(1L, LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3), SlotStatusEnum.AVAILABLE));

            Schedule schedule = Schedule.blockSchedule(scheduleId, scheduleDate, times);

            assertEquals(ScheduleStatusEnum.BLOCKED, schedule.getStatus());

        }

        @Test
        @DisplayName("Deve lança exceção se o agendamento for nulo")
        void shouldThrowExceptionIfScheduleIsNull() {

            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                Schedule.blockSchedule(null, null, null);
            });

            assertNotNull(exception);
            assertEquals(ResourceMessage.SCHEDULE_NOT_NULL.getMessage(), exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Teste de comportamento do cancelamento do agendamento")
    class CancelScheduleTest {

        @Test
        @DisplayName("Deve cancelar o agendamento com sucesso")
        void shouldCancelSchedule() {
            Long scheduleId = 1L;
            LocalDateTime scheduleDate = LocalDateTime.now();
            List<Slot> times = new ArrayList<>();

            times.add(Slot.createSlot(1L, LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3), SlotStatusEnum.AVAILABLE));

            Schedule canceledSchedule = Schedule.cancelSchedule(scheduleId, scheduleDate, times);

            assertEquals(ScheduleStatusEnum.CANCELLED, canceledSchedule.getStatus());

        }

        @Test
        @DisplayName("Deve lança exceção se o agendamento for nulo")
        void shouldThrowExceptionIfScheduleIsNull() {

            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                Schedule.cancelSchedule(null, null, null);
            });

            assertNotNull(exception);
            assertEquals(ResourceMessage.SCHEDULE_NOT_NULL.getMessage(), exception.getMessage());
        }

    }

    @Nested
    @DisplayName("Teste de comportamento do Builder do agendamento")
    class BuildScheduleTest {

        @Test
        @DisplayName("Deve criar um builder do agendamento com sucesso")
        void shouldCreateScheduleBuilder() {
            List<Slot> times = List.of(
                    Slot.createSlot(1L, LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3), SlotStatusEnum.AVAILABLE)
            );
            Schedule scheduleBuilder = new Schedule.Builder()
                    .scheduleId(1L)
                    .scheduleDate(LocalDateTime.now())
                    .status(ScheduleStatusEnum.ACTIVE)
                    .times(times)
                    .build();

            assertNotNull(scheduleBuilder);
        }
    }
}
