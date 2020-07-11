# 1 -intro

- O projeto corrente tem por objetivo reter a responsabilidade de registro de usuários (estoquista ou cliente) e o login da aplicação
- O banco de dados utilizado aqui é o **MongoDB** e na camada de Service é utilizado o **Spring Cache com Redis** para otimizar o acesso aos possíveis acessos, i.e; ***registros de novos usuários/acessos e a obtenção de logins alteram e/ou acessam primeiramente o cache***
- Os casos de uso abaixo determinam quais os serviços RESTful são expostos

---

#### 1.1 - execução

As definições de propriedades e configurações de porta ***(atual 8181)*** do servidor da aplicação corrente, do banco de dados **MongoDB** e **Redis** estão no artefato ```application.yml```

**MongoDB**

    host: localhost
    port: 27017
    database: fiapSampleLogin

**Redis**

    host: localhost
    port: 6379

---

#### 1.2 - domínios

##### ***[login: User]***
O único domínio presente neste módulo é o User, que gerencia as informações;
- ***login*** campo mandatório, 4 caracteres alfabéticos em minúsculo
- ***type*** (enum UserType[customer|stock]) campo mandatório, deve ter um dos dois valores; stock, customer
- ***id*** campo mandatório gerido internamente, identifica um usuário, seja ele UserType stock  

Este domínio é persistido permanentemente na base de dados **MongoDB** e mantido dentro de um TTL de **2min** em cache, através do **Spring Cache com Redis**.

---

# 2 - casos de uso e seus endpoints [use case]

Abaixo segue a lista de casos de uso e exemplos de requisições e respostas;  

#### 2.1 - [use case: cadastrar usuário/login: DONE]
- O caso de uso para cadastrar um usuário recebe como payload as definições básicas do novo usuário, contendo as informações;
    - ***login*** (deve conter 4 caracteres alfabéticos em minúsculo), campo mandatório
    - ***type*** (deve ter um dos dois valores; stock, customer), campo mandatório
- Caso já exista um registro com o mesmo login e type fornecidos, exceção de duplicidade deve ser lançada e com mensagem de erro explicando do problema e status code ***409 Conflict***
- Caso não exista nenhum registro até o momento para o novo login e type fornecidos, o registro deve ser persistido/criado, um código com 24 caracteres alfanuméricos é gerado e devolvido com status ***201 Created***

```$ curl localhost:8181/login -d '{"login":"abcd","type":"stock"}' -H 'Content-Type: application/json' ```

```
[request]
PUT /login
{
    "login": "abcd",
    "type": "stock"
}

[response]
201 Created
5ef958b02994931e98c15366
```

#

#### 2.2 - [use case: fazer login: DONE]
- Para fazer login, basta que seja fornecido como path variable os dados type e login
    - ***login*** (deve conter 4 caracteres alfabéticos em minúsculo), campo mandatório
    - ***type*** (deve ter um dos dois valores; stock, customer), campo mandatório
- O código com 24 caracteres alfanuméricos referentes ao usuário é devolvido com status ***200 Ok***

```$ curl localhost:8181/login/stock/abcd```

```
[request]
GET /login/stock/abcd

[response]
200 Ok
5ef958b02994931e98c15366
```

#
