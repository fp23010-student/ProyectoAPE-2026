-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost
-- Tiempo de generación: 28-05-2026
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `negocio`
--

-- --------------------------------------------------------

CREATE TABLE `categoria` (
  `idCategoria` int(2) UNSIGNED ZEROFILL NOT NULL,
  `descripcion` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `categoria` (`idCategoria`, `descripcion`) VALUES
(01, 'Entradas'),
(02, 'Platos fuertes'),
(03, 'Bebidas'),
(04, 'Postres'),
(05, 'Extras');

-- --------------------------------------------------------

CREATE TABLE `combo` (
  `idCombo` int(3) UNSIGNED ZEROFILL NOT NULL,
  `combo` varchar(150) NOT NULL,
  `precioCombo` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `combo` (`idCombo`, `combo`, `precioCombo`) VALUES
(001, 'COMBO CUATE (2 PERSONAS)',          14.50),
(002, 'PAQUETE TAQUERO FAMILIAR',          26.99),
(003, 'ALMUERZO EJECUTIVO MEXICANO',        6.50);

-- --------------------------------------------------------

CREATE TABLE `detalle` (
  `idCombo` int(3) UNSIGNED ZEROFILL NOT NULL,
  `idProducto` int(3) UNSIGNED ZEROFILL NOT NULL,
  `cantidad` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `detalle` (`idCombo`, `idProducto`, `cantidad`) VALUES
-- COMBO CUATE: 2 órdenes de tacos al pastor + 2 horchatas
(001, 003, 2),
(001, 007, 2),
-- PAQUETE TAQUERO FAMILIAR: tacos asada, tacos pastor, tacos birria, nachos, pichel jamaica
(002, 001, 1),
(002, 003, 1),
(002, 004, 1),
(002, 005, 1),
(002, 008, 1),
-- ALMUERZO EJECUTIVO: burrito + horchata
(003, 006, 1),
(003, 007, 1);

-- --------------------------------------------------------

CREATE TABLE `detalleorden` (
  `idOrden` int(11) NOT NULL,
  `idLinea` int(11) NOT NULL,
  `idProducto` int(3) UNSIGNED ZEROFILL DEFAULT NULL,
  `idCombo` int(3) UNSIGNED ZEROFILL DEFAULT NULL,
  `cantidad` int(11) NOT NULL,
  `precioUnitario` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

CREATE TABLE `mesa` (
  `idMesa` int(11) NOT NULL,
  `numero` int(11) NOT NULL,
  `capacidad` int(11) DEFAULT NULL,
  `estado` varchar(20) DEFAULT 'LIBRE'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `mesa` (`idMesa`, `numero`, `capacidad`, `estado`) VALUES
(1, 1, 4, 'LIBRE'),
(2, 2, 4, 'LIBRE'),
(3, 3, 6, 'LIBRE'),
(4, 4, 2, 'LIBRE'),
(5, 5, 6, 'LIBRE');

-- --------------------------------------------------------

CREATE TABLE `orden` (
  `idOrden` int(11) NOT NULL,
  `fechaHora` datetime DEFAULT current_timestamp(),
  `idUsuario` int(11) NOT NULL,
  `idMesa` int(11) DEFAULT NULL,
  `total` decimal(10,2) DEFAULT 0.00,
  `estado` varchar(20) DEFAULT 'PENDIENTE'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

CREATE TABLE `producto` (
  `idProducto` int(3) UNSIGNED ZEROFILL NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `descripcion` varchar(250) NOT NULL,
  `precio` decimal(10,2) NOT NULL,
  `idCategoria` int(2) UNSIGNED ZEROFILL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `producto` (`idProducto`, `nombre`, `descripcion`, `precio`, `idCategoria`) VALUES
-- Entradas (01)
(001, 'NACHOS SUPREMOS',           'Totopos con queso fundido, frijoles, guacamole, crema y jalapeños.',          6.50, 01),
(002, 'QUESADILLA DE QUESO OAXACA','Tortilla de harina con queso Oaxaca derretido y pico de gallo.',              4.25, 01),
-- Platos fuertes (02)
(003, 'ORDEN DE TACOS AL PASTOR',  '3 tacos en tortilla de maíz con carne al pastor, piña, cilantro y cebolla.', 5.50, 02),
(004, 'ORDEN DE TACOS DE ASADA',   '3 tacos de carne de res asada con cilantro y cebolla picada.',               5.75, 02),
(005, 'ORDEN DE TACOS DE BIRRIA',  '3 tacos de birria de res con queso derretido y consomé caliente.',           6.95, 02),
(006, 'BURRITO CALIFORNIANO',      'Tortilla gigante con carne, arroz, frijoles, queso y aguacate.',             6.25, 02),
-- Bebidas (03)
(007, 'HORCHATA MEXICANA',         'Bebida de arroz con canela y vainilla.',                                      1.75, 03),
(008, 'AGUA DE JAMAICA (PICHEL)',  'Pichel de refresco natural de flor de Jamaica para compartir.',               4.50, 03),
(009, 'SODA LATA',                 'Gaseosa en lata 355ml (Coca-Cola, Tropical, Sprite).',                        1.50, 03),
-- Postres (04)
(010, 'CHURROS TRADICIONALES',     '3 churros con azúcar y canela acompañados de cajeta.',                       3.50, 04),
(011, 'FLAN NAPOLITANO CASERO',    'Porción de flan cremoso bañado en caramelo artesanal.',                       2.75, 04),
-- Extras (05)
(012, 'PORCION DE GUACAMOLE',      'Aguacate fresco con limón, cilantro y sal.',                                  1.50, 05),
(013, 'EXTRA QUESO O SALSAS',      'Porción extra de queso fundido o salsa picante de la casa.',                  0.75, 05);

-- --------------------------------------------------------

CREATE TABLE `rol` (
  `idRol` int(11) NOT NULL,
  `rol` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `rol` (`idRol`, `rol`) VALUES
(1, 'ADMINISTRADOR'),
(2, 'SUPERVISOR'),
(3, 'COCINERO'),
(4, 'MESERO');

-- --------------------------------------------------------

CREATE TABLE `usuario` (
  `idUsuario` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `apellido` varchar(100) NOT NULL,
  `usuario` varchar(50) NOT NULL,
  `contrasenia` varchar(255) NOT NULL,
  `idRol` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `usuario` (`idUsuario`, `nombre`, `apellido`, `usuario`, `contrasenia`, `idRol`) VALUES
(1, 'Hamilton', 'Figueroa',  'IngeHamil',    '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 1),
(2, 'Carlos',   'Martinez',  'carlosmesero', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 4),
(3, 'Ana',      'Lopez',     'anasuper',     '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 2);

-- --------------------------------------------------------
-- ÍNDICES
-- --------------------------------------------------------

ALTER TABLE `categoria`   ADD PRIMARY KEY (`idCategoria`);
ALTER TABLE `combo`       ADD PRIMARY KEY (`idCombo`);
ALTER TABLE `detalle`     ADD PRIMARY KEY (`idCombo`,`idProducto`), ADD KEY `idProducto` (`idProducto`);
ALTER TABLE `detalleorden`
  ADD PRIMARY KEY (`idOrden`,`idLinea`),
  ADD UNIQUE KEY `idOrden`   (`idOrden`,`idProducto`),
  ADD UNIQUE KEY `idOrden_2` (`idOrden`,`idCombo`),
  ADD KEY `idProducto` (`idProducto`),
  ADD KEY `idCombo`    (`idCombo`);
ALTER TABLE `mesa`    ADD PRIMARY KEY (`idMesa`);
ALTER TABLE `orden`   ADD PRIMARY KEY (`idOrden`), ADD KEY `idUsuario` (`idUsuario`), ADD KEY `orden_ibfk_2` (`idMesa`);
ALTER TABLE `producto` ADD PRIMARY KEY (`idProducto`), ADD KEY `idCategoria` (`idCategoria`);
ALTER TABLE `rol`     ADD PRIMARY KEY (`idRol`), ADD UNIQUE KEY `rol` (`rol`);
ALTER TABLE `usuario` ADD PRIMARY KEY (`idUsuario`), ADD UNIQUE KEY `usuario` (`usuario`), ADD KEY `idRol` (`idRol`);

-- --------------------------------------------------------
-- AUTO_INCREMENT
-- --------------------------------------------------------

ALTER TABLE `mesa`    MODIFY `idMesa`    int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
ALTER TABLE `orden`   MODIFY `idOrden`   int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;
ALTER TABLE `rol`     MODIFY `idRol`     int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
ALTER TABLE `usuario` MODIFY `idUsuario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

-- --------------------------------------------------------
-- FOREIGN KEYS
-- --------------------------------------------------------

ALTER TABLE `detalle`
  ADD CONSTRAINT `detalle_ibfk_1` FOREIGN KEY (`idCombo`)     REFERENCES `combo`    (`idCombo`),
  ADD CONSTRAINT `detalle_ibfk_2` FOREIGN KEY (`idProducto`)  REFERENCES `producto` (`idProducto`);

ALTER TABLE `detalleorden`
  ADD CONSTRAINT `detalleorden_ibfk_1` FOREIGN KEY (`idOrden`)    REFERENCES `orden`    (`idOrden`) ON DELETE CASCADE,
  ADD CONSTRAINT `detalleorden_ibfk_2` FOREIGN KEY (`idProducto`) REFERENCES `producto` (`idProducto`),
  ADD CONSTRAINT `detalleorden_ibfk_3` FOREIGN KEY (`idCombo`)    REFERENCES `combo`    (`idCombo`);

ALTER TABLE `orden`
  ADD CONSTRAINT `orden_ibfk_1` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`idUsuario`),
  ADD CONSTRAINT `orden_ibfk_2` FOREIGN KEY (`idMesa`)    REFERENCES `mesa`    (`idMesa`);

ALTER TABLE `producto`
  ADD CONSTRAINT `producto_ibfk_1` FOREIGN KEY (`idCategoria`) REFERENCES `categoria` (`idCategoria`);

ALTER TABLE `usuario`
  ADD CONSTRAINT `usuario_ibfk_1` FOREIGN KEY (`idRol`) REFERENCES `rol` (`idRol`);

COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
