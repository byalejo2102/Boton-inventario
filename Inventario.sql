-- Cambiar el esquema de base de datos en PostgreSQL
-- El comando 'use cierre;' no es necesario en PostgreSQL, en su lugar, usa la conexión a la base de datos adecuada.

-- Creación de la tabla UNIDADES_MEDIDA
CREATE TABLE UNIDADES_MEDIDA (
   UMCODIGO             CHAR(3) NOT NULL,
   UMDESCRIPCION        VARCHAR(100) NOT NULL,
   PRIMARY KEY (UMCODIGO)
);

-- Insertar ejemplos de unidades de medida
INSERT INTO UNIDADES_MEDIDA (UMCODIGO, UMDESCRIPCION) VALUES
('kg', 'Kilogramos'),    -- Para peso
('gr', 'Gramos'),        -- Para peso
('lt', 'Litros'),        -- Para volumen líquido
('ml', 'Mililitros'),    -- Para volumen líquido
('un', 'Unidades'),      -- Para conteo de artículos
('m', 'Metros'),         -- Para longitud
('cm', 'Centímetros'),   -- Para longitud
('mm', 'Milímetros');    -- Para longitud

-- Creación de la tabla ESTADOS_PRODUCTO
CREATE TABLE ESTADOS_PRODUCTO (
   ESTCODIGO            CHAR(3) NOT NULL,
   ESTDESCRIPCION       VARCHAR(100) NOT NULL,
   PRIMARY KEY (ESTCODIGO)
);

-- Insertar ejemplos de estados de producto
INSERT INTO ESTADOS_PRODUCTO (ESTCODIGO, ESTDESCRIPCION) VALUES
('ACT', 'Activo'),       -- Disponible para la venta o uso
('INA', 'Inactivo'),     -- No disponible temporalmente
('DIS', 'Discontinuado'),-- No volverá a estar disponible
('ESC', 'Escasez');      -- Poca cantidad disponible

-- Creación de la tabla PRODUCTOS
CREATE TABLE PRODUCTOS (
   PROCODIGO            VARCHAR(10) NOT NULL,
   PRODESCRIPCION       VARCHAR(100) NOT NULL,
   PROUNIDADMEDIDA      CHAR(3) NOT NULL,
   PROSALDOINICIAL      NUMERIC(9,2) NOT NULL CHECK (PROSALDOINICIAL >= 0),
   PROINGRESOS          NUMERIC(9,2) NOT NULL CHECK (PROINGRESOS >= 0),
   PROEGRESOS           NUMERIC(9,2) NOT NULL CHECK (PROEGRESOS >= 0),
   PROAJUSTES           NUMERIC(9,2) NOT NULL CHECK (PROAJUSTES >= 0),
   PROSALDOFINAL        NUMERIC(9,2) NOT NULL CHECK (PROSALDOFINAL >= 0),
   PROCOSTOUM           NUMERIC(7,2) NOT NULL CHECK (PROCOSTOUM >= 0),
   PROPRECIOUM          NUMERIC(7,2) NOT NULL CHECK (PROPRECIOUM >= 0),
   PROSTATUS            CHAR(3) NOT NULL,
   PROFOTO              BYTEA,
   PRIMARY KEY (PROCODIGO),
   CONSTRAINT fk_unidad_medida FOREIGN KEY (PROUNIDADMEDIDA) REFERENCES UNIDADES_MEDIDA(UMCODIGO),
   CONSTRAINT fk_prostatus FOREIGN KEY (PROSTATUS) REFERENCES ESTADOS_PRODUCTO(ESTCODIGO)
);

ALTER TABLE PRODUCTOS ADD COLUMN PROFOTO BYTEA;

-- Insertar ejemplos de productos
INSERT INTO PRODUCTOS (
    PROCODIGO, PRODESCRIPCION, PROUNIDADMEDIDA, PROSALDOINICIAL, PROINGRESOS, PROEGRESOS,
    PROAJUSTES, PROSALDOFINAL, PROCOSTOUM, PROPRECIOUM, PROSTATUS
) VALUES
    ('P-0001', 'CEREAL TRIGO ENTERO', 'un', 100, 50, 20, 5, 135, 0.50, 1.00, 'ACT'),
    ('P-0002', 'MORA FRUTO COMPLETO', 'un', 200, 100, 50, 10, 260, 0.20, 0.45, 'ACT'),
    ('P-0003', 'CARNE DE CERDO CON HUESO', 'kg', 150, 70, 30, 0, 190, 3.50, 5.00, 'ACT'),
    ('P-0004', 'SARDINAS EN CONSERVA', 'un', 300, 150, 75, 25, 400, 0.85, 1.50, 'ACT'),
    ('P-0005', 'LECHE LÍQUIDA PASTEURIZADA', 'lt', 500, 300, 200, 0, 600, 0.90, 1.20, 'ACT'),
    ('P-0006', 'ATÚN EN CONSERVA', 'un', 400, 200, 100, 50, 550, 0.95, 1.75, 'ACT'),
    ('P-0007', 'JUGO DE NARANJA', 'lt', 250, 120, 80, 0, 290, 0.55, 1.10, 'ACT'),
    ('P-0008', 'HARINA DE TRIGO', 'kg', 800, 400, 300, 50, 950, 0.20, 0.40, 'ACT'),
    ('P-0009', 'ARROZ BLANCO', 'un', 600, 300, 150, 30, 780, 0.15, 0.30, 'ACT'),
    ('P-0010', 'FRIJOLES NEGROS', 'kg', 500, 250, 125, 25, 650, 0.45, 0.90, 'ACT');

-- Alterar tabla PRODUCTOS para agregar columna PROSALDOFISICO
ALTER TABLE PRODUCTOS ADD COLUMN PROSALDOFISICO NUMERIC(9,2);

-- Seleccionar todos los registros de la tabla PRODUCTOS
SELECT * FROM PRODUCTOS;

-- Creación de la tabla AJUSTES
CREATE TABLE AJUSTES (
	AJUCODIGO	CHAR(7)		NOT NULL,
    AJUFECHA	DATE		NOT NULL,
    AJUDESCRIPCION	CHAR(50)	NOT NULL,
    PRIMARY KEY(AJUCODIGO)
);

-- Insertar ejemplos de ajustes
INSERT INTO AJUSTES (AJUCODIGO, AJUFECHA, AJUDESCRIPCION) VALUES 
('AJ-0001', '2024-06-19', 'Ajuste por inventario anual'),
('AJ-0002', '2024-06-20', 'Ajuste por error en entrada de mercancía'),
('AJ-0003', '2024-06-21', 'Ajuste por pérdida de producto');

-- No es necesario en PostgreSQL, ya que no usa LONGBLOB
-- ALTER TABLE PRODUCTOS MODIFY COLUMN PROFOTO LONGBLOB;

-- Seleccionar todos los registros de la tabla PRODUCTOS
SELECT * FROM PRODUCTOS;

-- Creación de la tabla PXA
CREATE TABLE PXA (
	PROCODIGO	CHAR(9)			NOT NULL,
    AJUCODIGO	CHAR(7)			NOT NULL,
    PXADESCRIPCION VARCHAR(100)	NOT NULL,
    PXAUNIDADMEDIDA	CHAR(3)		NOT NULL,
    PXAFECHA	DATE			NOT NULL,
    PXASALDOINICIAL	NUMERIC(9,2)	NOT NULL,
    PXAINGRESOS	NUMERIC(9,2)	NOT NULL,
    PXAEGRESOS	NUMERIC(9,2)	NOT NULL,
    PXAAJUSTES	NUMERIC(9,2)	NOT NULL,
    PXASALDOFINAL	NUMERIC(9,2)	NOT NULL,
    PXASALDOFISICO	NUMERIC(9,2)	NOT NULL,
    PXASALDOTOTAL NUMERIC(9,2)	NOT NULL,
    PRIMARY KEY(PROCODIGO, AJUCODIGO),
    CONSTRAINT FK_PXA_PROCODIGO FOREIGN KEY (PROCODIGO) REFERENCES PRODUCTOS (PROCODIGO),
    CONSTRAINT FK_PXA_AJUCODIGO FOREIGN KEY (AJUCODIGO) REFERENCES AJUSTES (AJUCODIGO)
);

-- Paso 1: Eliminar la restricción de clave foránea
ALTER TABLE PXA DROP CONSTRAINT FK_PXA_AJUCODIGO;

-- Paso 2: Modificar el tamaño de la columna AJUCODIGO
ALTER TABLE AJUSTES ALTER COLUMN AJUCODIGO TYPE VARCHAR(20);

-- Paso 3: Volver a agregar la restricción de clave foránea
ALTER TABLE PXA ADD CONSTRAINT FK_PXA_AJUCODIGO FOREIGN KEY (AJUCODIGO) REFERENCES AJUSTES(AJUCODIGO);

-- Actualizar la columna PROSALDOFISICO
UPDATE PRODUCTOS SET PROSALDOFISICO = 0 WHERE PROSALDOFISICO IS NULL AND PROCODIGO IS NOT NULL;

-- Selecciones específicas
SELECT * FROM AJUSTES WHERE AJUDESCRIPCION = 'Cierre de inventario';
SELECT * FROM PXA WHERE AJUCODIGO = (SELECT AJUCODIGO FROM AJUSTES WHERE AJUDESCRIPCION = 'Cierre de inventario');
SELECT * FROM PXA WHERE PROCODIGO = 'P-0002';

-- Seleccionar todos los registros de las tablas
SELECT * FROM AJUSTES;
SELECT * FROM PXA;
SELECT * FROM PRODUCTOS;

