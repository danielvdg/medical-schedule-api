package br.com.codart.domain.entities.slot;

import br.com.codart.src.domain.Enum.SlotStatusEnum;
import br.com.codart.src.domain.entities.Slot;
import br.com.codart.src.service.message.ResourceMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


class SlotTest {

    @Nested
    @DisplayName("Teste de comportamento da classe")
    class SlotBehaviorTest {

        @Test
        @DisplayName("Deve Criar Slot")
        void shouldCreateSlot() {
           Slot slotCreated = Slot.createSlot(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), SlotStatusEnum.AVAILABLE);
            assertNotNull(slotCreated);
        }

        @Test
        @DisplayName("Deve Bloquear Slot")
        void ShouldBlockSlotExpired() {
            Slot slot = Slot.createSlot(1L, LocalDateTime.now().minusHours(2), LocalDateTime.now().minusHours(1), SlotStatusEnum.AVAILABLE);

            Exception exception = assertThrows(IllegalStateException.class, slot::blockSlot);

            assertEquals(ResourceMessage.SLOT_EXPIRED.getMessage(), exception.getMessage());
        }

        @Nested
        @DisplayName("Teste de comportamento do cancelamento do slot")
        class CancelSlotBehaviorTest {

            @Test
            @DisplayName("Deve Cancelar Slot")
            void shouldCancelSlot() {
                Slot slot = Slot.createSlot(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), SlotStatusEnum.RESERVED);
                slot.cancelSlot();
                assertEquals(SlotStatusEnum.CANCELLED, slot.getSlotStatus());
            }

            @Test
            @DisplayName("Deve lançar exceção se o slot status estiver Bloqueado")
            void shouldThrowsExceptionSlotStatusBlocked() {
                Slot slot = Slot.createSlot(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), SlotStatusEnum.BLOCKED);

                Exception exception = assertThrows(IllegalStateException.class, slot::cancelSlot);

                assertEquals(ResourceMessage.SLOT_BLOCKED.getMessage(), exception.getMessage());
            }

            @Test
            @DisplayName("Deve lançar exceção se o slot status estiver Disponível")
            void shouldThrowsExceptionSlotStatusAvailable() {
                Slot slot = Slot.createSlot(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), SlotStatusEnum.AVAILABLE);

                Exception exception = assertThrows(IllegalStateException.class, slot::cancelSlot);

                assertEquals(ResourceMessage.SLOT_BLOCKED.getMessage(), exception.getMessage());
            }

        }

        @Test
        @DisplayName("Deve Reservar Slot")
        void testReserveSlotSuccess() {
            Slot slot = Slot.createSlot(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), SlotStatusEnum.AVAILABLE);

            slot.reserveSlot();

            assertEquals(SlotStatusEnum.RESERVED, slot.getSlotStatus(), "O slot deveria estar reservado.");
        }

        @Test
        @DisplayName("Deve Reservar Slot com Slot em andamento")
        void shouldReserveSlotInvalidState() {
            Slot slot = Slot.createSlot(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), SlotStatusEnum.RESERVED);

            Exception exception = assertThrows(IllegalStateException.class, slot::reserveSlot);

            assertEquals(ResourceMessage.NOT_AVAILABLE.getMessage(), exception.getMessage());
        }

        @Test
        @DisplayName("Deve Reagendar Slot")
        void shouldRescheduleSlotSuccess() {
            Slot slot = Slot.createSlot(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), SlotStatusEnum.RESERVED);

            LocalDateTime newStartTime = LocalDateTime.now().plusHours(3);
            LocalDateTime newEndTime = LocalDateTime.now().plusHours(4);

            slot.rescheduleSlot(newStartTime, newEndTime, 60);

            assertEquals(newStartTime, slot.getStartTime(), "O horário de início deveria ter sido atualizado.");
            assertEquals(newEndTime, slot.getEndTime(), "O horário de fim deveria ter sido atualizado.");
            assertEquals(SlotStatusEnum.RESCHEDULED, slot.getSlotStatus(), "O slot deveria estar reagendado.");
        }

        @Test
        @DisplayName("Deve Reagendar Slot com Slot em andamento")
        void shouldRescheduleSlotInvalidState() {
            Slot slot = Slot.createSlot(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), SlotStatusEnum.AVAILABLE);

            Exception exception = assertThrows(IllegalStateException.class, () ->
                    slot.rescheduleSlot(LocalDateTime.now().plusHours(3), LocalDateTime.now().plusHours(4), 60)
            );

            assertEquals(ResourceMessage.NOT_RESERVED.getMessage(), exception.getMessage());
        }

        @Nested
        @DisplayName("Teste de comportamento do método de reabertura")

        class reopenSlotBehaviorTest {

            @Test
            @DisplayName("Deve Reabrir Slot status CANCELLED")
            void testReopenSlotWithCancelledSuccess() {
                Slot slot = Slot.createSlot(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), SlotStatusEnum.CANCELLED);

                slot.reopenSlot();

                assertEquals(SlotStatusEnum.AVAILABLE, slot.getSlotStatus(), "O slot deveria estar reaberto.");
            }

            @Test
            @DisplayName("Deve Reabrir Slot status BLOCKED")
            void testReopenSlotWithBlockedSuccess() {
                Slot slot = Slot.createSlot(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), SlotStatusEnum.BLOCKED);

                slot.reopenSlot();

                assertEquals(SlotStatusEnum.AVAILABLE, slot.getSlotStatus(), "O slot deveria estar reaberto.");
            }

            @Test
            @DisplayName("Deve lançar exceção se o slot status for diferente de CANCELLED e BLOCKED")
            void shouldThrowsExceptionSlotStatusBlocked() {
                Slot slot = Slot.createSlot(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), SlotStatusEnum.AVAILABLE);

                Exception exception = assertThrows(IllegalStateException.class, slot::reopenSlot);

                assertEquals(ResourceMessage.SLOT_BLOCKED.getMessage(), exception.getMessage());
            }


        }


        @Nested
        @DisplayName("Teste de comportamento do método de bloqueio")
        class blockSlotBehaviorTest {
            @Test
            @DisplayName("Deve Bloquear Slot")
            void shouldBlockSlotSuccess() {
                Slot slot = Slot.createSlot(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), SlotStatusEnum.AVAILABLE);

                slot.blockSlot();

                assertEquals(SlotStatusEnum.BLOCKED, slot.getSlotStatus(), "O slot deveria estar bloqueado.");
            }

            @Test
            @DisplayName("Deve lançar exceção se o slot status estiver CANCELLED")
            void shouldThrowsExceptionSlotStatusCancelled() {

                Slot slot = Slot.createSlot(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), SlotStatusEnum.CANCELLED);

                Exception exception = assertThrows(IllegalStateException.class, slot::blockSlot);

                assertEquals(ResourceMessage.SLOT_BLOCKED.getMessage(), exception.getMessage());
            }

            @Test
            @DisplayName("Deve lançar exceção se o slot status estiver Reservado")
            void shouldThrowsExceptionSlotStatusReserved() {

                Slot slot = Slot.createSlot(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), SlotStatusEnum.RESERVED);

                Exception exception = assertThrows(IllegalStateException.class, slot::blockSlot);

                assertEquals(ResourceMessage.SLOT_BLOCKED.getMessage(), exception.getMessage());
            }

            @Test
            @DisplayName("Deve lançar exceção se o slot status estiver Reagendado")
            void shouldThrowsExceptionSlotStatusReschedule() {
                Slot slot = Slot.createSlot(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), SlotStatusEnum.RESCHEDULED);

                Exception exception = assertThrows(IllegalStateException.class, slot::blockSlot);

                assertEquals(ResourceMessage.SLOT_BLOCKED.getMessage(), exception.getMessage());
            }

        }

    }

    @Nested
    @DisplayName("Teste de atributos da classe")
    class TestAtributesClass {
        @Test
        @DisplayName("Deve Obter o ID do Slot")
        void shouldGetSlotId() {
            Slot slot = Slot.createSlot(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), SlotStatusEnum.RESERVED);

            assertEquals(1L, slot.getSlotId(), "O ID do slot deveria ser 1.");
        }

        @Test
        @DisplayName("Deve Obter o Status do Slot")
        void shouldGetSlotStatus() {
            Slot slot = Slot.createSlot(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), SlotStatusEnum.RESERVED);
            assertEquals(SlotStatusEnum.RESERVED, slot.getSlotStatus(), "O slot deveria estar reservado.");
        }

        @Test
        @DisplayName("Deve Reagendar Slot com Slot em andamento")
        void shouldrescheduleSlotThowsException() {
            Slot slot = Slot.createSlot(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), SlotStatusEnum.RESERVED);

            LocalDateTime newStartTime = LocalDateTime.now();
            LocalDateTime newEndTime = LocalDateTime.now().plusHours(4);
            Exception exception = assertThrows(IllegalArgumentException.class, () -> slot.rescheduleSlot(newStartTime, newEndTime, -2));
            assertEquals(ResourceMessage.NOT_AVAILABLE.getMessage(), exception.getMessage());
        }

        @Test
        @DisplayName("Deve Obter o toString do Slot")
        void shouldGetToString() {
            Slot slot = Slot.createSlot(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), SlotStatusEnum.RESERVED);

            assertEquals(String.format("Slot{slotId=%d, startTime=%s, endTime=%s, slotStatus=%s}",
                    slot.getSlotId(), slot.getStartTime(), slot.getEndTime(), slot.getSlotStatus()), slot.toString());
        }
    }

    @Nested
    @DisplayName("Teste de construtor da classe")
    class SlotConstructorTest {
        @Test
        @DisplayName("Teste de construtor da classe criação com sucesso")
        void testConstructor() {
            Slot slot = Slot.createSlot(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), SlotStatusEnum.RESERVED);

            assertEquals(1L, slot.getSlotId(), "O ID do slot deveria ser 1.");
            assertEquals(SlotStatusEnum.RESERVED, slot.getSlotStatus(), "O slot deveria estar reservado.");
        }

        @Test
        @DisplayName("Teste com construtor erro de horário de fim menor que o horário de início")
        void shouldThrowExceptionCreateSlotWithNullValues() {

            Exception exception = assertThrows(IllegalArgumentException.class, () ->
                    Slot.createSlot(null, null, null, null));

            assertNotNull(exception);
            assertEquals(ResourceMessage.SLOT_INVALID.getMessage(), exception.getMessage());
        }

        @Test
        @DisplayName("Teste construtor com ID nulo")
        void shouldThrowExceptionCreateSlotWithIdNull() {

            LocalDateTime startTime = LocalDateTime.now().plusHours(2);
            LocalDateTime endTime = LocalDateTime.now().plusHours(3);
            SlotStatusEnum slotStatus = SlotStatusEnum.RESERVED;

            Exception exception = assertThrows(IllegalArgumentException.class, () ->
                    Slot.createSlot(null, startTime, endTime, slotStatus));

            assertNotNull(exception);
            assertEquals(ResourceMessage.SLOT_INVALID.getMessage(), exception.getMessage());
        }

        @Test
        @DisplayName("Teste construtor com horário de início nulo")
        void slotConstructorWithStartTimeNull() {
            Long slotId = 1L;
            LocalDateTime endTime = LocalDateTime.now().plusHours(2);
            SlotStatusEnum slotStatus = SlotStatusEnum.RESERVED;

            Exception exception = assertThrows(IllegalArgumentException.class, () ->
                    Slot.createSlot(slotId, null, endTime, slotStatus));

            assertNotNull(exception);
            assertEquals(ResourceMessage.SLOT_INVALID.getMessage(), exception.getMessage());
        }

        @Test
        @DisplayName("Teste construtor com horário de fim nulo")
        void shouldThrowExceptionCreateSlotWithEndTimeNull() {

            Long slotId = 1L;
            LocalDateTime startTime = LocalDateTime.now().plusHours(2);
            SlotStatusEnum slotStatus = SlotStatusEnum.RESERVED;

            Exception exception = assertThrows(IllegalArgumentException.class, () ->
                    Slot.createSlot(slotId, startTime, null, slotStatus));

            assertNotNull(exception);
            assertEquals(ResourceMessage.SLOT_INVALID.getMessage(), exception.getMessage());
        }

        @Test
        @DisplayName("Teste construtor com status nulo")
        void shouldThrowExceptionCreateSlotWithSlotStatusNull() {
            Long slotId = 1L;
            LocalDateTime startTime = LocalDateTime.now().plusHours(2);
            LocalDateTime endTime = LocalDateTime.now().plusHours(3);

            Exception exception = assertThrows(IllegalArgumentException.class, () ->
                    Slot.createSlot(slotId, startTime, endTime, null));

            assertNotNull(exception);
            assertEquals(ResourceMessage.SLOT_INVALID.getMessage(), exception.getMessage());
        }

        @Test
        @DisplayName("Teste construtor com ID negativo")
        void shouldThrowExceptionIDNegative() {

            LocalDateTime startTime = LocalDateTime.now().plusHours(2);
            LocalDateTime endTime = LocalDateTime.now().plusHours(3);
            SlotStatusEnum slotStatus = SlotStatusEnum.RESERVED;

            Exception exception = assertThrows(IllegalArgumentException.class, () ->
                    Slot.createSlot(-1L, startTime, endTime, slotStatus));

            assertNotNull(exception);
            assertEquals(ResourceMessage.SLOT_INVALID.getMessage(), exception.getMessage());
        }

        @Test
        @DisplayName("Teste com construtor erro de horário de fim menor que o horário de início")
        void testConstructorInvalidEndTime() {

            LocalDateTime startTime = LocalDateTime.now().plusHours(2);
            LocalDateTime endTime = LocalDateTime.now().plusHours(1);
            Long slotId = 1L;

            Exception exception = assertThrows(IllegalArgumentException.class, () ->
                    Slot.createSlot(slotId, startTime, endTime, SlotStatusEnum.RESERVED));

            assertNotNull(exception);
            assertEquals(ResourceMessage.SLOT_INVALID.getMessage(), exception.getMessage());
        }

    }

    @Test
    @DisplayName("Deve cria Builder do slot")
    void shouldCreateSlotBuilder() {

        Slot slot = new Slot.Builder()
                .slotId(1L)
                .startTime(LocalDateTime.now().plusHours(1))
                .endTime(LocalDateTime.now().plusHours(2))
                .slotStatus(SlotStatusEnum.RESERVED)
                .build();

        assertEquals(1L, slot.getSlotId(), "O ID do slot deveria ser 1.");
        assertEquals(SlotStatusEnum.RESERVED, slot.getSlotStatus(), "O slot deveria estar reservado.");

    }

}
