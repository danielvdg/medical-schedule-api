A classe Slot é responsável por representar um intervalo de tempo que pode ser agendado em uma agenda médica. As regras de negócio que regem o comportamento e a validação dos slots são descritas a seguir:

Definição do Slot

Um slot é um intervalo de tempo que pode ser agendado para um paciente ou bloqueado para uso administrativo.
Cada slot possui os seguintes atributos:
slotId: Um identificador único gerado automaticamente.
startTime: O horário de início do slot.
endTime: O horário de término do slot.
slotStatus: O status atual do slot.

O slot pode assumir os seguintes estados:
AVAILABLE: Livre para agendamento.
RESERVED: Reservado para um paciente.
BLOCKED: Bloqueado para uso administrativo.
CANCELLED: Cancelado e indisponível para agendamento.
RESCHEDULED: Reagendado para um novo horário.
Regras de Criação

Um slot deve ser criado com um horário de início (startTime) e uma duração, resultando no horário de término (endTime).
A duração fornecida não pode ser negativa.
Ao criar um slot, seu status inicial deve ser AVAILABLE.
Restrições de Operações com Slots

Bloquear um Slot
Um slot não pode ser bloqueado se estiver em um dos seguintes estados:
RESERVED: Já reservado para um paciente.
CANCELLED: Cancelado.
RESCHEDULED: Já reagendado.

Cancelar um Slot
Um slot não pode ser cancelado se estiver em um dos seguintes estados:
BLOCKED: Já bloqueado.
AVAILABLE: Livre para agendamento.

Reservar um Slot
Um slot só pode ser reservado se estiver no estado AVAILABLE.

Reagendar um Slot
Apenas slots no estado RESERVED podem ser reagendados.

Reabrir um Slot
Apenas slots nos estados CANCELLED ou BLOCKED podem ser reabertos, desde que não estejam expirados.

Validação Temporal
Qualquer operação que altere o status de um slot deve verificar se o horário atual (currentTime) é anterior ao horário de término (endTime) do slot.
Slots expirados não podem ser alterados.

Validação de Consistência
A criação ou modificação de um slot deve passar por uma validação interna de modo que NUNCA seja possível criar ou modificar uma instância para um estado inválido

Testes Unitários
Os testes unitários devem cobrir pelo menos 80% de todo o código dessa classe.

## PALAVRAS CHAVES

* POO
* IMUTABILIDADE
* PROTEÇÃO DA INVARIÂNCIA
* ENCAPSULAMENTO
* STATIC FACTORY METHOD
* QUALIDADE DE CÓDIGO
* RESPONSABILIDADE ÚNICA