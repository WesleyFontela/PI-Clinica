CREATE DATABASE clinica_db;
USE clinica_db;

-- Tabela de pacientes
CREATE TABLE paciente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    telefone VARCHAR(20)
);

-- Tabela de médicos
CREATE TABLE medico (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    especialidade VARCHAR(100),
    crm VARCHAR(20) UNIQUE NOT NULL
);

-- Tabela de usuários (login do sistema)
CREATE TABLE usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    login VARCHAR(50) UNIQUE NOT NULL,
    senha VARCHAR(100) NOT NULL,
    perfil VARCHAR(20) NOT NULL
);

-- Tabela de consultas
CREATE TABLE consulta (
    id INT AUTO_INCREMENT PRIMARY KEY,
    paciente_id INT NOT NULL,
    medico_id INT NOT NULL,
    dataAgendada DATE NOT NULL,
    horaAgendada TIME NOT NULL,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (paciente_id) REFERENCES paciente(id),
    FOREIGN KEY (medico_id) REFERENCES medico(id)
);

-- Inserir pacientes
INSERT INTO paciente (nome, cpf, telefone) VALUES
('João Silva', '111.111.111-11', '99999-1111'),
('Maria Souza', '222.222.222-22', '99999-2222'),
('Carlos Pereira', '333.333.333-33', '99999-3333');

-- Inserir médicos
INSERT INTO medico (nome, especialidade, crm) VALUES
('Dr. Carlos', 'Cardiologia', 'CRM123'),
('Dra. Ana', 'Pediatria', 'CRM456'),
('Dr. Roberto', 'Ortopedia', 'CRM789');

-- Inserir usuários
INSERT INTO usuario (login, senha, perfil) VALUES
('admin', '123', 'ADMIN'),
('ana', '123', 'MEDICO'),
('carlos', '123', 'MEDICO'),
('roberto', '123', 'MEDICO'),
('recep', '123', 'RECEP');

-- Inserir consultas
INSERT INTO consulta (paciente_id, medico_id, dataAgendada, horaAgendada, status) VALUES
(1, 1, '2025-11-25', '10:00:00', 'AGENDADA'),
(2, 2, '2025-11-26', '14:30:00', 'AGENDADA'),
(3, 3, '2025-11-27', '09:00:00', 'REALIZADA'),
(1, 2, '2025-11-28', '11:15:00', 'CANCELADA');