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
    @DisplayName("Teste de comportamentos da classe")
    class SlotBehaviorTest {

        @Test
        void testBlockSlotExpired() {
            Slot slot = new Slot(1L, LocalDateTime.now().minusHours(2), LocalDateTime.now().minusHours(1), SlotStatusEnum.AVAILABLE);
            Exception exception = assertThrows(IllegalStateException.class, slot::blockSlot);

            assertEquals(ResourceMessage.SLOT_EXPIRED.getMessage(), exception.getMessage());
        }

        @Test
        void testCancelSlotInvalidState() {
            Slot slot = new Slot(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), SlotStatusEnum.AVAILABLE);

            Exception exception = assertThrows(IllegalStateException.class, slot::cancelSlot);

            assertEquals(ResourceMessage.SLOT_BLOCKED.getMessage(), exception.getMessage());
        }

        @Test
        void testReserveSlotSuccess() {
            Slot slot = new Slot(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), SlotStatusEnum.AVAILABLE);

            slot.reserveSlot();

            assertEquals(SlotStatusEnum.RESERVED, slot.getSlotStatus(), "O slot deveria estar reservado.");
        }

        @Test
        void testReserveSlotInvalidState() {
            Slot slot = new Slot(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), SlotStatusEnum.BLOCKED);

            Exception exception = assertThrows(IllegalStateException.class, slot::reserveSlot);

            assertEquals(ResourceMessage.NOT_AVAILABLE.getMessage(), exception.getMessage());
        }

        @Test
        void testRescheduleSlotSuccess() {
            Slot slot = new Slot(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), SlotStatusEnum.RESERVED);

            LocalDateTime newStartTime = LocalDateTime.now().plusHours(3);
            LocalDateTime newEndTime = LocalDateTime.now().plusHours(4);

            slot.rescheduleSlot(newStartTime, newEndTime, 60);

            assertEquals(newStartTime, slot.getStartTime(), "O horário de início deveria ter sido atualizado.");
            assertEquals(newEndTime, slot.getEndTime(), "O horário de fim deveria ter sido atualizado.");
            assertEquals(SlotStatusEnum.RESCHEDULED, slot.getSlotStatus(), "O slot deveria estar reagendado.");
        }

        @Test
        void testRescheduleSlotInvalidState() {
            Slot slot = new Slot(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), SlotStatusEnum.AVAILABLE);

            Exception exception = assertThrows(IllegalStateException.class, () ->
                    slot.rescheduleSlot(LocalDateTime.now().plusHours(3), LocalDateTime.now().plusHours(4), 60)
            );

            assertEquals(ResourceMessage.NOT_RESERVED.getMessage(), exception.getMessage());
        }

        @Test
        void testReopenSlotSuccess() {
            Slot slot = new Slot(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), SlotStatusEnum.CANCELLED);

            slot.reopenSlot();

            assertEquals(SlotStatusEnum.AVAILABLE, slot.getSlotStatus(), "O slot deveria estar reaberto.");
        }

        @Test
        void testReopenSlotInvalidState() {
            Slot slot = new Slot(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), SlotStatusEnum.RESERVED);

            Exception exception = assertThrows(IllegalStateException.class, slot::reopenSlot);

            assertEquals(ResourceMessage.SLOT_BLOCKED.getMessage(), exception.getMessage());
        }

        @Test
        void testBlockSlotSuccess() {
            Slot slot = new Slot(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), SlotStatusEnum.AVAILABLE);

            slot.blockSlot();

            assertEquals(SlotStatusEnum.BLOCKED, slot.getSlotStatus(), "O slot deveria estar bloqueado.");
        }

        @Test
        void testCancelSlotSuccess() {
            Slot slot = new Slot(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), SlotStatusEnum.RESERVED);

            slot.cancelSlot();

            assertEquals(SlotStatusEnum.CANCELLED, slot.getSlotStatus(), "O slot deveria estar cancelado.");
        }

    }

    @Nested
    @DisplayName("Teste de atributos da classe")
    class TestAtributesClass {
        @Test
        void testGetSlotId() {
            Slot slot = new Slot(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), SlotStatusEnum.RESERVED);
            assertEquals(1L, slot.getSlotId(), "O ID do slot deveria ser 1.");
        }

        @Test
        void TestGetSlotStatus() {
            Slot slot = new Slot(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), SlotStatusEnum.RESERVED);
            assertEquals(SlotStatusEnum.RESERVED, slot.getSlotStatus(), "O slot deveria estar reservado.");
        }

        @Test
        void testrescheduleSlotThowsException() {
            Slot slot = new Slot(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), SlotStatusEnum.RESERVED);
            LocalDateTime newStartTime = LocalDateTime.now();
            LocalDateTime newEndTime = LocalDateTime.now().plusHours(4);
            Exception exception = assertThrows(IllegalArgumentException.class, () -> slot.rescheduleSlot(newStartTime, newEndTime, -2));
            assertEquals(ResourceMessage.NOT_AVAILABLE.getMessage(), exception.getMessage());
        }

        @Test
        void testToString() {
            Slot slot = new Slot(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), SlotStatusEnum.RESERVED);
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
            Slot slot = new Slot(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), SlotStatusEnum.RESERVED);

            assertEquals(1L, slot.getSlotId(), "O ID do slot deveria ser 1.");
            assertEquals(SlotStatusEnum.RESERVED, slot.getSlotStatus(), "O slot deveria estar reservado.");
        }

        @Test
        @DisplayName("Teste com construtor erro de horário de fim menor que o horário de início")
        void testConstructorInvalidEndTime() {

            LocalDateTime startTime = LocalDateTime.now().plusHours(2);
            LocalDateTime endTime = LocalDateTime.now().plusHours(1);
            Long slotId = 1L;

            Exception exception = assertThrows(IllegalArgumentException.class, () ->
                    new Slot(slotId, startTime, endTime, SlotStatusEnum.RESERVED));

            assertNotNull(exception);
        }
    }

}
