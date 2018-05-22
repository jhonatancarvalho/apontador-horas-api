# API | Apontador de Horas
Disponível em: https://apontador-horas-api.herokuapp.com/api/v1/

### Tempo estimado / gasto
| Atividade | Estimativa | Gasto |
| ------ | ------ | ------ |
| Cadastro de Usuários | 3 h | 2h 18m |
| Autenticação | 3 h | 2h 56m |
| Cadastro de Projetos | 3 h | 2h 39m |
| Cadastro de Tempo | 3 h | 3h 35m |
##### Relatório da ferramenta de controle de horas (Toggl)
![alt text](https://s3-sa-east-1.amazonaws.com/jhonatan-carvalho/tempoGasto.png)

### Tecnologias utilizadas
* [Java] - Linguagem de programação interpretada orientada a objetos.
* [Spring Boot] - Projeto da Spring que utiliza o Spring Framework para facilitar o processo de configuração como persistência de dados e segurança. Para a api foram utilizados as dependencias do Spring a seguir, o data-jpa para persistência de dados, o starter-security com JWT para para autenticação/autorização e o starter-test para testes de integração.
* [PostgreSQL] - Banco de dados para armazenamento em ambiente de produção.
* [H2] - Banco de dados embarcado, é utilizado na api em ambiente de teste.

 [Java]: <https://www.java.com/pt_BR/>
 [Spring Boot]: <https://projects.spring.io/spring-boot/>
 [PostgreSQL]: <https://www.postgresql.org/>
 [H2]: <http://www.h2database.com/html/main.html/>

### Endpoints
Apenas os endpoints de POST login e users são públicos, para acessar os demais endpoints é necessário enviar um HEADER Authorization nas requisições com o valor retornado ao fazer o login.

##### - Login
__[POST]__ https://apontador-horas-api.herokuapp.com/api/v1/login - Efetua autenticação, é retornado um JWT no HEADER Authorization, exemplo de body:
```sh
{
	"login": "jhonatan.carvalho",
	"password": "123"
}
```

##### - Usuário
__[GET]__ https://apontador-horas-api.herokuapp.com/api/v1/users/{id} - Retorna um usuário pelo id.

__[POST]__ https://apontador-horas-api.herokuapp.com/api/v1/users - Cadastra um novo usuário, exemplo de body:
```sh
{
	"name": "Jhonatan Carvalho",
	"email": "jhonatan.carvalho92@gmail.com",
	"login": "jhonatan.carvalho",
	"password": "123"
}
```

__[PUT]__ https://apontador-horas-api.herokuapp.com/api/v1/users/{id} - Altera um usuário, exemplo de body:
```sh
{
	"name": "Nome alterado",
	"email": "jhonatan.carvalho92@gmail.com",
	"login": "jhonatan.carvalho",
	"password": "123"
}
```

##### - Projeto
__[GET]__ https://apontador-horas-api.herokuapp.com/api/v1/projects/{id} - Retorna um projeto pelo id.

__[POST]__ https://apontador-horas-api.herokuapp.com/api/v1/projects - Cadastra um novo projeto, exemplo de body:
```sh
{
	"title": "Projeto One",
	"description": "Projeto com manipulação de objetos",
	"user_id": [1, 2, 3]
}
```

__[PUT]__ https://apontador-horas-api.herokuapp.com/api/v1/projects/{id} - Altera um projeto, exemplo de body:
```sh
{
	"title": "Projeto One",
	"description": "Alterado descrição do projeto",
	"user_id": [1, 2, 3]
}
```

__[GET]__ https://apontador-horas-api.herokuapp.com/api/v1/projects - Retorna todos os projetos.

##### - Tempo
__[POST]__ https://apontador-horas-api.herokuapp.com/api/v1/times - Cadastra um novo tempo, exemplo de body:
```sh
{
    "project_id": 6,
    "started_at": "2018-03-05T10:42:08.233",
    "ended_at": "2018-03-08T10:42:08.233"
}
```

__[PUT]__ https://apontador-horas-api.herokuapp.com/api/v1/times/{id} - Altera um tempo, exemplo de body:
```sh
{
    "project_id": 6,
    "started_at": "2018-03-04T10:42:08.233",
    "ended_at": "2018-03-08T10:42:08.233"
}
```

__[GET]__ https://apontador-horas-api.herokuapp.com/api/v1/times/{project_id} - Retorna todos os tempo de um projeto.
