# 1 -intro

- o projeto corrente tem por responsabilidade manter registros dos produtos disponíveis **(cadastrados no fiap.stock.mgnt)**, de endereços e pedidos dos clientes, que acabam vendo os produtos e solicitando seus pedidos através deste projeto corrente.
- quando um cliente seleciona produtos e conclui um pedido, este ultimo é disparado para o projeto **fiap.stock.mgnt** para que lá o estoquista tenha a possibilidade de confirmar a preseça ou não do produto, lançando neste uma confirmação ou rejeição do pedido.
- o banco de dados utilizado é o **MongoDB** e na camada de Service apenas para o domínio de Produto é utilizado o **Spring Cache com Redis** para otimizar seu encontro rápido por código, que é necessário quando chega um pedido.
- os casos de uso abaixo determinam quais os serviços RESTful são expostos

---

#### 1.1 - execução

as definições de propriedades e configurações de porta ***(atual 8383)*** do servidor da aplicação corrente, do banco de dados **MySQL** e **Redis** estão no artefato ```application.yml```

**MongoDB**

    host: localhost
    port: 27017
    database: fiapStockPortal

**Redis**

    host: localhost
    port: 6379

---

#### 1.2 - domínios

##### ***[portal: Product]***
O domínio produto, mesmo no módulo **fiap.stock.portal** é gerenciado apenas pelo usuário estoquista, pois na verdade depende de uma ação que veio de uma interação no módulo **fiap.stock.mgnt**.  
No portal, as informações de um documento de uma coleção é sumarizado trazido das informações tabular no **fiap.stock.mgnt**.  
O documento aqui armezenado retém e apresenta os dados;
- ***login_id*** campo mandatório, 25 caracteres alfanuméricos que identifica o usuário estoquista que fez o registro nos produtos
- ***code*** 
- ***price*** 
- ***quantity*** 
- ***description*** definido no catálogo do estoque, mantido apenas no módulo **fiap.stock.mgnt**
  
Este domínio é persistido permanentemente na base de dados **MongoDB**, e é gerido no cache afim de ser buscado rapidamente por ***code***, com operações PUT, EVICT e um TTL de **2min**, cache mantido através do **Spring Cache com Redis**.

#

##### ***[portal: Address]***
O documento para o domínio de endereço detém os dados;
- ***login_id*** campo mandatório, 25 caracteres alfanuméricos que identifica o usuário cliente que faz o cadastro de um endereço para si
- ***zipCode***
- ***complement***
- ***city***
- ***state***
- ***country***

Este domínio é persistido permanentemente na base de dados **MySQL**, sem nenhum reflexo/gerencia em cache.

#

##### ***[portal: Order]***
O documento para o domínio de pedidos, desnormalizado detém os dados;
- ***login_id*** campo mandatório, 25 caracteres alfanuméricos que identifica o usuário cliente que faz o pedido
- ***code*** gerido e fornecido pelo módulo, é formado pelo prefixo **"ORD-"** e 7 digitos (apenas números)
- ***entryDate*** data inferida no momento da inserção/persistência do pedido
- ***status*** status do pedido junto ao estoque
- ***products*** lista de produtos selecionados no pedido, com os códigos e quantidades dos produtos
- ***address*** endereço de entrega

Este domínio é persistido permanentemente na base de dados **MySQL**, sem nenhum reflexo/gerencia em cache.

---

# 2 - casos de uso e seus endpoints [use case]

Abaixo segue a lista de casos de uso e exemplos de requisições e respostas;  

#### 2.1 - [use case: estoquista adiciona/atualiza um produto ao portal: DONE]
- O payload postado pelo estoquista através de uma ação no projeto **fiap.stock.mgnt** deve ser carregado no **fiap.stock.portal** com os mesmos dados, numa operação de ***UPSERT***, ou seja, se ainda não existe este produto no **fiap.stock.portal**, então este deve ser inserido, e caso exista, deve ser totalmente substituído/sobrescrito.
    - para fazer a validação de ***UPSERT***, é necessário logicamente procurar por um produto com o mesmo ***code***, então inferir se deve ser inserido ou sobrescrito.
- As informações esperadas para um domínio Produto no **fiap.stock.portal** são;
    - ***code***
    - ***description***
    - ***price***
    - ***quantity***
- A informação ***loginId*** deverá ser recebida via path variable, e refere-se a identificação do estoquista (UserType stock), o que quer dizer que o valor de um login válido efetuado via módulo ***fiap.sample.login*** deve ter sido obtido
    - ***loginId***
        - [validar] deve ser verificado se o ***loginId*** é de fato válido para o tipo (UserType) 'stock'
- Caso registro armazenado com sucesso, devolver resposta com corpo completo e status ***201 Created***

```$ curl localhost:8383/portal/users/5ef958b02994931e98c15366/products -d '{ "code": "PRD-9876543", "description": "sample product description", "price": 253.63, "quantity": 24 }' -H 'Content-Type: application/json' ```

```
[request]
POST portal/users/5ef958b02994931e98c15366/products
{
    "code": "PRD-9876543",
    "description": "sample product description",
    "price": 253.63,
    "quantity": 24
}

[response]
201 Created
5ef958b02994931e98c15366
```

#

#### 2.2 - [use case: lista produtos no portal, acessível a estoquista ou cliente: DONE]
- Carregar toda a lista de produtos registrada no **fiap.stock.portal**
- Os dados carregados no payload para o domínio Produto devem ser;
    - ***code***
    - ***description***
    - ***price***

```$ curl localhost:8383/portal/users/5ef958b02994931e98c15366/products -H 'Content-Type: application/json' ```

```
[request]
GET portal/users/5ef958b02994931e98c15366/products

[response]
200 Ok
{
    "products": [
        {
            "code": "PRD-9876543",
            "description": "sample product description",
            "price": 253.63
        },
        {
            "code": "PRD-123456",
            "description": "another product",
            "price": 14.01
        }
    ]
}
```

#

#### 2.3 - [use case: carrega determinado produto baseado no seu code, acessível a estoquista ou cliente: DONE]
- Carregar todos os dados de um domínio Produto, baseado no seu code
- Dados do produto;
    - ***code***
    - ***description***
    - ***price***
    - ***quantity***
    
```$ curl localhost:8383/portal/users/5ef958b02994931e98c15366/products/PRD-9876543 -H 'Content-Type: application/json' ```

```
[request]
GET portal/users/5ef958b02994931e98c15366/products/PRD-9876543

[response]
200 Ok
{
    "code": "PRD-9876543",
    "description": "sample product description",
    "price": 253.63,
    "quantity": 24
}
```

#

#### 2.4 - [use case: adiciona um registro de endereço para o cliente: DONE]
- Adiciona um registro de endereço para um cliente
- Payload com os dados;
    - ***zipCode***
    - ***complement***
    - ***city***
    - ***state***
    - ***country***
- A informação ***loginId*** deverá ser recebida via path variable, e refere-se a identificação do cliente (UserType customer), o que quer dizer que o valor de um login válido efetuado via módulo ***fiap.sample.login*** deve ter sido obtido
    - ***loginId***
        - [validar] deve ser verificado se o ***loginId*** é de fato válido para o tipo (UserType) 'customer'
- O resultado deve ter o payload com os dados adicionais, e devolvido com status ***201 Created***
    - ***code*** (código gerido internamente)

```$ curl localhost:8383/portal/users/5ef9589c2994931e98c15365/addresses -d '{ "zip_code": "12345-678", "complement": "Cond Azul, Bl A Apt 123", "city": "São Paulo", "state": "São Paulo", "country": "Brasil" }' -H 'Content-Type: application/json' ```

```
[request]
POST portal/users/5ef9589c2994931e98c15365/addresses
{
    "zip_code": "12345-678",
    "complement": "Cond Azul, Bl A Apt 123",
    "city": "São Paulo",
    "state": "São Paulo",
    "country": "Brasil"
}

[response]
201 Created
{
    "code": "5ff958bGH994931e98c15364",
    "zip_code": "123456-789",
    "complement": "Cond Azul, Bl A Apt 123",
    "city": "São Paulo",
    "state": "São Paulo",
    "country": "Brasil"
}
```

#

#### 2.5 - [use case: lista todos os registros de endereço para o cliente: DONE]
- Lista todos os registros de endereço para o cliente
- Payload com os dados;
    - ***code*** (código gerido internamente)
    - ***zipCode***
    - ***complement***
    - ***city***
    - ***state***
    - ***country***
- A informação ***loginId*** deverá ser recebida via path variable, e refere-se a identificação do cliente (UserType customer), o que quer dizer que o valor de um login válido efetuado via módulo ***fiap.sample.login*** deve ter sido obtido
    - ***loginId***
        - [validar] deve ser verificado se o ***loginId*** é de fato válido para o tipo (UserType) 'customer'
        - [validar] a listagem só pode trazer registros de endereços específicos do usuário corrente, logo deve estar na clausula de acesso ao registro persistido
        
```$ curl localhost:8383/portal/users/5ef9589c2994931e98c15365/addresses ```

```
[request]
GET portal/users/5ef9589c2994931e98c15365/addresses

[response]
200 Ok
{
    "addresses": [
        {
            "code": "5ff958bGH994931e98c15364",
            "zip_code": "123456-789",
            "complement": "Cond Azul, Bl A Apt 123",
            "city": "São Paulo",
            "state": "São Paulo",
            "country": "Brasil"
        },
        {
            "code": "5GG958bGH994931e98c14253",
            "zip_code": "654321-987",
            "complement": "Rua Santos, Nr 49",
            "city": "Sorocaba",
            "state": "São Paulo",
            "country": "Brasil"
        }
    ]
}
```

#

#### 2.6 - [use case: remoção de um registro de endereço para o cliente: DONE]
- Remoção de um registro de endereço para o cliente
- A identificação do registro de endereço a ser removido deve ser fornecido via path variable
- Payload com os dados;
    - ***code***
- A informação ***loginId*** deverá ser recebida via path variable, e refere-se a identificação do cliente (UserType customer), o que quer dizer que o valor de um login válido efetuado via módulo ***fiap.sample.login*** deve ter sido obtido
    - ***loginId***
        - [validar] deve ser verificado se o ***loginId*** é de fato válido para o tipo (UserType) 'customer'
        - [validar] a listagem só pode acessar registro de endereços específicos do usuário corrente, logo deve estar na clausula de acesso ao registro persistido
        
```$ curl -X DELETE localhost:8383/portal/users/5ef9589c2994931e98c15365/addresses/5ff958bGH994931e98c15364 ```

```
[request]
DELETE portal/users/5ef9589c2994931e98c15365/addresses/5ff958bGH994931e98c15364

[response]
200 Ok
```

#

#### 2.7 - [use case: cliente adiciona um pedido: DONE]
- Adição de novo pedido para o cliente corrente
- O payload na requisição é composto dos campos
    - ***products[].code*** (campo mandatório) um array de objetos com campo ***code***, que deve corresponder ao ***code*** do domínio ***Product***
    - ***products[].quantity*** (campo mandatório) ainda no array supracitado, a quantidade desejada na compra/pedido, que deve ser menor ou igual a quantidade disponível
    - ***address*** deve ser um dos endereços já registrados no **fiap.stock.portal**
- A informação ***loginId*** deverá ser recebida via path variable, e refere-se a identificação do cliente (UserType customer), o que quer dizer que o valor de um login válido efetuado via módulo ***fiap.sample.login*** deve ter sido obtido
    - ***loginId***
        - [validar] deve ser verificado se o ***loginId*** é de fato válido para o tipo (UserType) 'customer'
- O resultado deve ter o payload com os dados adicionais, e devolvido com status ***201 Created***
    - ***code*** (campo obrigatório) gerido e fornecido pelo módulo
        - é formado pelo prefixo **"ORD-"** e 7 digitos (apenas números)
- [IMPORTANTE] ao inserir um pedido no módulo **fiap.stock.portal** é necessário que este dispare uma requisição para o **fiap.stock.mgnt** com os dados deste pedido, para que este pedido chegue na fila do estoquista, e lá este confirmará ou rejeitará o pedido.

```$ curl localhost:8383/portal/users/5ef9589c2994931e98c15365/orders -d '{ "products": [ { "code": "PRD-0000001", "quantity": 10 }, { "code": "PRD-0000002", "quantity": 5 } ], "address": { "zip_code": "12345-678", "complement": "Rua Santos, Nr 49", "city": "Sorocaba", "state": "São Paulo", "country": "Brasil" } }' -H 'Content-Type: application/json' ```

```
[request]
POST portal/users/5ef9589c2994931e98c15365/orders
{
    "products": [
        {
            "code": "PRD-0000001",
            "quantity": 10
        },
        {
            "code": "PRD-0000002",
            "quantity": 5
        }
    ],
    "address": {
       "zip_code": "12345-678",
       "complement": "Rua Santos, Nr 49",
       "city": "Sorocaba",
       "state": "São Paulo",
       "country": "Brasil"
   }
}

[response]
201 Created
{
    "code": "ORD-4569877",
    "entry_time": "2019-01-29T05:18:56",
    "status": "WAITING_FOR_ANSWER",
    "products": [
        {
            "code": "PRD-9876543",
            "quantity": 10
        },
        {
            "code": "PRD-654987",
            "quantity": 5
        }
    ],
    "address": {
       "code": "5GG958bGH994931e98c14253",
       "zip_code": "654321-987",
       "complement": "Rua Santos, Nr 49",
       "city": "Sorocaba",
       "state": "São Paulo",
       "country": "Brasil"
   }
}
```

#

#### 2.8 - [use case: estoquista atualiza status do pedido do cliente no portal: DONE]
- O payload na requisição é composto dos campos
    - ***status*** (campo mandatório) o estado desta ordem perante o módulo **fiap.stock.mgnt**
- A informação ***code*** deverá ser recebida via path variable, e identifica a qual pedido o estoquista está alterando
- A informação ***loginId*** deverá ser recebida via path variable, e refere-se a identificação do estoquista (UserType stock)
    - ***loginId***
        - [validar] deve ser verificado se o ***loginId*** é de fato válido para o tipo (UserType) 'stock'

```$ curl -X PUT localhost:8383/portal/users/5ef9589c2994931e98c15365/orders/ORD-0000001 -d '{ "status": "APPROVED" }' -H 'Content-Type: application/json' ```

```
[request]
PUT portal/users/5ef9589c2994931e98c15365/orders/ORD-0000001
{
    "status": "APPROVED"
}

[response]
200 Ok
```

#

#### 2.9 - [use case: cliente solicita listagem de todos seus pedidos já efetuados: DONE]
- Cliente deve ter a capacidade de listar todos os pedidos efetuados, independentemente de seus status/estados;
    - WAITING_FOR_ANSWER aguardando conferência do estoquista
    - APPROVED aprovado no estoque
    - REJECTED reprovado no estoque
- A informação ***loginId*** deverá ser recebida via path variable, e refere-se a identificação do cliente (UserType customer), o que quer dizer que o valor de um login válido efetuado via módulo ***fiap.sample.login*** deve ter sido obtido
    - ***loginId***
        - [validar] deve ser verificado se o ***loginId*** é de fato válido para o tipo (UserType) 'customer'

```$ curl localhost:8383/portal/users/5ef9589c2994931e98c15365/orders ```

```
[request]
GET portal/users/5ef9589c2994931e98c15365/orders

[response]
200 Ok
{
    "orders": [
        {
            "code": "ORD-4569877",
            "entry_date": "2019-01-29T05:18:56",
            "status": "WAITING_FOR_ANSWER",
            "products: [
                {
                    "code": "PRD-9876543",
                    "quantity": 10
                },
                {
                    "code": "PRD-654987",
                    "quantity": 5
                }
            ],
            "address": {
               "code": "5GG958bGH994931e98c14253",
               "zip_code": "654321-987",
               "complement": "Rua Santos, Nr 49",
               "city": "Sorocaba",
               "state": "São Paulo",
               "country": "Brasil"
           }
        },
        {
            "code": "ORD-1122336",
            "entry_date": "2018-10-03T17:23:46",
            "status": "APPROVED",
            "products: [
                {
                    "code": "PRD-1122331",
                    "quantity": 1
                }
            ],
            "address": {
               "code": "5ff958bGH994931e98c15364",
               "zip_code": "123456-789",
               "complement": "Cond Azul, Bl A Apt 123",
               "city": "São Paulo",
               "state": "São Paulo",
               "country": "Brasil"
           }
        }
    ]
}
```

#
