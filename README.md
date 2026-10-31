
# 🎬 CineTrack

**Personal Movie Tracker** — Um sistema de gerenciamento pessoal de filmes desenvolvido em Java.

O CineTrack é uma aplicação de console criada para organizar coleções cinematográficas, permitindo cadastrar filmes, registrar avaliações, adicionar resenhas pessoais e acompanhar os títulos assistidos ou que ainda estão na watchlist.

## ✨ Funcionalidades

- Cadastro e gerenciamento de filmes.
- Listagem de filmes cadastrados.
- Pesquisa por título.
- Organização por gênero.
- Registro de avaliações pessoais.
- Adição de comentários e resenhas.
- Controle de filmes assistidos e pendentes.
- Cálculo de estatísticas da coleção.

## 🛠️ Tecnologias utilizadas

- **Java 21+** — Linguagem principal.
- **Programação Orientada a Objetos (POO)** — Organização do código.
- **ArrayList** — Gerenciamento de coleções.
- **Scanner** — Entrada de dados pelo terminal.
- **Git e GitHub** — Versionamento de código.

## 📁 Estrutura do projeto

```text
CineTrack/
│
├── src/
│   ├── enums/
│   │   └── StatusFilme.java
│   │
│   ├── model/
│   │   └── Filme.java
│   │
│   ├── service/
│   │   └── FilmeService.java
│   │
│   ├── util/
│   │   └── Estatisticas.java
│   │
│   └── Main.java
│
└── .gitignore
```

## 🚀 Como executar

### Pré-requisitos

- Java JDK 21 ou superior.
- Visual Studio Code ou outra IDE compatível com Java.

### 1. Clone o repositório

```bash
git clone https://github.com/ingridalvesfarias/CineTrack.git
```

### 2. Acesse a pasta do projeto

```bash
cd CineTrack
```

### 3. Compile o projeto

```bash
javac -d out -sourcepath src src/Main.java
```

### 4. Execute a aplicação

```bash
java -cp out Main
```

## 🎯 Objetivo

O CineTrack foi desenvolvido como projeto de aprendizado em Java, com o objetivo de colocar em prática conceitos de lógica de programação, orientação a objetos, organização de código e gerenciamento de dados.

## 🔮 Melhorias futuras

- Persistência de dados com MySQL.
- Integração com JDBC.
- Desenvolvimento de uma API REST com Spring Boot.
- Integração com a API TMDB para consulta de filmes.
- Desenvolvimento de uma interface web.

---

**Desenvolvido por Ingrid Farias**

*Projeto pessoal de estudos em Java.*
