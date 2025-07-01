CREATE DATABASE IF NOT EXISTS `db_oficina_mecanica`
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE `db_oficina_mecanica`;

CREATE TABLE `Cliente` (
    `id_cliente` INT AUTO_INCREMENT PRIMARY KEY,
    `nome_completo` VARCHAR(150) NOT NULL,
    `cpf` VARCHAR(14) NOT NULL UNIQUE,
    `telefone` VARCHAR(20) NOT NULL,
    `email` VARCHAR(100) UNIQUE,
    `cep` VARCHAR(10) NOT NULL,
    `endereco` VARCHAR(255)
);

CREATE TABLE funcionario (
    id_funcionario INT AUTO_INCREMENT PRIMARY KEY,
    nome_completo VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    telefone VARCHAR(15),
    email VARCHAR(100),
    cargo VARCHAR(100) NOT NULL,
    salario DECIMAL(10, 2) NOT NULL
);

CREATE TABLE `Servico` (
    `id_servico` INT AUTO_INCREMENT PRIMARY KEY,
    `nome_servico` VARCHAR(100) NOT NULL UNIQUE,
    `descricao` TEXT,
    `preco_padrao` DECIMAL(10, 2) NOT NULL
);

CREATE TABLE `Veiculo` (
    `id_veiculo` INT AUTO_INCREMENT PRIMARY KEY,
    `id_cliente` INT NOT NULL,
    `placa` VARCHAR(10) NOT NULL UNIQUE,
    `marca` VARCHAR(50) NOT NULL,
    `modelo` VARCHAR(50) NOT NULL,
    `ano_fabricacao` INT,
    `cor` VARCHAR(30),
    FOREIGN KEY (`id_cliente`) REFERENCES `Cliente`(`id_cliente`) ON DELETE CASCADE
);

CREATE TABLE `Ordem_Servico` (
    `id_ordem_servico` INT AUTO_INCREMENT PRIMARY KEY,
    `id_cliente` INT NOT NULL,
    `id_veiculo` INT NOT NULL,
    `id_funcionario_responsavel` INT NOT NULL,
    `data_entrada` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `data_saida_prevista` DATE,
    `data_saida_efetiva` DATE,
    `status` ENUM('Pendente', 'Em Andamento', 'Aguardando Peças', 'Concluído', 'Cancelado') NOT NULL,
    `valor_total` DECIMAL(10, 2) DEFAULT 0.00,
    `observacoes` TEXT,
    FOREIGN KEY (`id_cliente`) REFERENCES `Cliente`(`id_cliente`) ON DELETE RESTRICT,
    FOREIGN KEY (`id_veiculo`) REFERENCES `Veiculo`(`id_veiculo`) ON DELETE RESTRICT,
    FOREIGN KEY (`id_funcionario_responsavel`) REFERENCES `Funcionario`(`id_funcionario`) ON DELETE RESTRICT

);