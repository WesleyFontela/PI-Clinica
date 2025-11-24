# Sistema de Gestão de Consultas Médicas

**Versão:** 1.0  
**Status do Projeto:** Em desenvolvimento  
**Responsável:** Wesley Messina Fontela  

## Objetivo
Gerenciar o ciclo completo de atendimento em uma clínica, permitindo cadastro de pacientes e médicos, agendamento de consultas, controle de horários e geração de relatórios.

## Usuários do Sistema
- **Administrador**: acesso completo, gerencia pacientes, médicos, consultas e relatórios globais.  
- **Recepcionista**: cadastra pacientes/médicos, agenda consultas, edita registros e acessa relatórios gerais.  
- **Médico**: visualiza apenas suas consultas, atualiza status e acessa relatórios restritos.  

## Funcionalidades
- Cadastro de pacientes e médicos  
- Agendamento de consultas com validação de horário  
- Atualização de status da consulta (Agendada, Realizada, Cancelada)  
- Relatórios por paciente, médico, status e período  
- Pesquisa de pacientes e médicos  
- Controle de permissões por perfil  
- Mensagens de erro em operações inválidas  

## Tecnologias Utilizadas
- Java (Maven)  
- MySQL  
- JPA / Hibernate  
- Swing (Interface Gráfica)  

## Observações Técnicas
- Entidades principais: **Paciente**, **Medico**, **Consulta**, **StatusConsulta**  
- DAO genérico para padronizar operações de acesso a dados  
- Validação de disponibilidade de horários para evitar conflitos  