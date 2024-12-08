# Medical Schedule API

## Sobre o projeto

### API para agendamento de consultas médicas.

O medical-schedule-api nasceu de uma parceria entre o consultório Clínica Vitra Lavoisier e a empresa de tecnologia CODART para resolver os desafios do gerenciamento de agendamentos médicos. A iniciativa busca automatizar e otimizar o fluxo de trabalho do consultório, eliminando processos manuais que consomem tempo e reduzem a eficiência do atendimento.

### O Problema

Gestão manual de horários: O médico utilizava uma planilha para organizar os agendamentos, o que gerava inconsistências e aumentava a chance de erros.
Reagendamentos complexos: Cada alteração de horário ou cancelamento exigia tempo para encontrar uma nova data disponível e atualizar os registros manualmente.
Bloqueio de horários: Era necessário rastrear manualmente horários bloqueados, como férias ou reuniões, o que frequentemente causava conflitos no agendamento.
Falta de autonomia para os pacientes: Pacientes precisavam ligar para o consultório para cancelar ou reagendar consultas, sobrecarregando a equipe administrativa.
Falta de notificações automatizadas: Sem alertas automáticos, era comum que pacientes esquecessem os horários marcados, gerando faltas e ociosidade na agenda.
Essas dores resultavam em perda de tempo, insatisfação dos pacientes e maior carga de trabalho para o médico e sua equipe.

### Solução Proposta

A solução proposta é um sistema de agendamento de consultas médicas que automatiza o processo de agendamento e gerenciamento de horários, permitindo que os médicos e seus pacientes tenham mais tempo para se concentrar no atendimento.
O Sistema será divido inicialmente em 3 sub módulos
- **Schedule** - representando a agenda do médico
- **Slot** - representando os horários de atendimento de uma agenda
- **Appointment** - representando o agendamento do paciente

O sistema deve começar a ser desenvolvido pelo **Slot**

### Funcionalidades

- Agendamento de consultas
- Reagendamento de consultas
- Cancelamento de consultas
- Consulta de agendamentos
- Consulta de horários disponíveis
- Bloqueio de horários
- Notificação de agendamentos
- Notificação de cancelamentos
- Notificação de reagendamentos

### Tecnologias utilizadas

- Java 21
- Gradle
- Junit 5

