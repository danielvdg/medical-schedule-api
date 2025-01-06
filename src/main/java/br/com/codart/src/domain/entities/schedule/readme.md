## Atributos Principais
id: Identificador único da agenda.
scheduleDate: Data associada à agenda.
status: Estado atual da agenda (e.g., ativa, totalmente preenchida, etc.).
times: Lista de slots de horários configuráveis.

## Atributos e Construtores:

Garanta a inicialização consistente dos atributos.
Inclua validações necessárias para atributos obrigatórios.
Métodos de Fábrica:

Implemente as regras de negócio de acordo com os métodos descritos.
Manipulação de Slots:

Crie métodos para adicionar, bloquear, cancelar e reagendar slots, respeitando as restrições de domínio.
Validações:

Certifique-se de validar conflitos de horários e estados inválidos.
Status da Agenda:

Automatize o cálculo do estado da agenda com base nos slots.