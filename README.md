###### Escola: FIAP
###### Curso: MBA FULLSTACK DEVELOPER, MICROSERVICES, CLOUD & IoT
###### Aluno / RM: JEAN BRUNO SOUTO VILLETE / 335435

# 1 - intro

O repositório corrente mantém um conjunto de projetos que evolui de acordo com as matérias cursadas na ***FIAP***, no
 curso ***MBA FULLSTACK DEVELOPER, MICROSERVICES, CLOUD & IoT***.

A minha escolha foi o desenvolvimento de um exemplo fitício da gerencia de um estoque, a apresentação deste estoque em
 um portal, a solicitação de pedidos/compras neste portal, e a integração entre o atendimento por parte do estoquista,
 confirmando ou rejeitando pedidos de clientes.

Para cada matéria cursada, eu mantenho um artefato README.md correspondente, que descreve o que houve de contribução e
 evolução no projeto.

| MATÉRIA | PROFESSOR | LINK README |
| --- | --- | --- |
| PERSISTENCE | RAFAEL TSUJI MATSUYAMA | - |
| MICROSERVICES DEVELOPMENT | ANDRE PONTES SAMPAIO | [README-MICROSERVICES-DEVELOPMENT.md](README-MICROSERVICES-DEVELOPMENT.md) |
| DEVOPS | HELDER PEREIRA | [README-DEVOPS.md](README-DEVOPS.md) |
| INTEGRATIONS & DEVELOPMENT TOOLS | CARLOS VINICIUS MAGNABOSCHI HESKETH | [README-INTEGRATIONS-AND-DEVELOPMENT-TOOLS.md](README-INTEGRATIONS-AND-DEVELOPMENT-TOOLS.md) |

#
 
O módulo **fiap.sample.login** detém microserviços que atendem requisições REST, onde o estado é mantido num banco de
 dados MongoDB, e há uma camada de cache com Redis.

O único serviço exposto é a gerência de registros de usuário e seu tipo, onde também se atende uma requisição de login,
 devolvendo um identificador que é válido dentro do estoque e portal.

#

O módulo **fiap.stock.mgnt** mantém serviços CRUD para o domínio de Catálogo e Produtos, além da integração necessária
 com pedidos efetuados via **fiap.stock.portal**, que chegam no estoque e o estoquista podem confirmar ou rejeitar estes
 pedidos.

Este módulo é totalmente persistido no banco de dados MySQL, com modelo transacional e constraints, onde os dados estão
 normalizados e estruturados tabularmente.

A integração entre cadastro de produtos no estoque e apresentação no portal, de pedidos no portal e recebimento no
 estoque, além da aprovação destes pedidos no estoque (que devem ser atualizado nos registros de pedidos no portal) é
 feita via mensageria RabbitMQ.

#

Módulo **fiap.stock.portal** tem como objetivo oferecer serviços para o cliente do portal, como a navegação e
 visualização de produtos, solicitação de pedidos e depois o acompanhamento destes pedidos.
 
Este módulo mantém as informações num modelo não normalizado, mantido no banco de dados MongoDB, com seus os produtos e
 pedidos, além de um CRUD para os endereços dos clientes, que é depois utilizado nos próprios pedidos.

---

# 2 - execução com docker/docker-compose

Após o clone local (e/ou toda alteração em tempo de desenvolvimento), rode o script build que monta tanto os componentes
de backend quanto frontend e gera as suas imagens docker LOCALMENTE.
 
``` $ ./docker-build.sh ```

#### subindo separadamente os componentes backend com imagens docker LOCAIS
``` $ docker-compose -f docker-compose-local-backend-only.yml up ```

#### subindo separadamente os componentes frontend com imagens docker LOCAIS
``` $ docker-compose -f docker-compose-local-frontend-only.yml up ```

#### subindo componentes backend junto com frontend com imagens docker LOCAIS
``` $ docker-compose -f docker-compose-local.yml up ```

#### subindo componentes backend junto com frontend com imagens docker PÚBLICAS
``` $ docker-compose up ```

---

# 3 - diagrama de componentes e caso de uso

![](docs/fiap.stock.ecommerce.png)


---

# 4 - Play With Docker; explicação/fluxo

- Acesso, Login e Adição de nova Instância no ***Play with Docker***
- Clone do projeto  
- Subindo módulos via docker-compose  

[googledrive/1-setup.mp4](https://drive.google.com/file/d/1xXusUzAhK5KOtgqe1dV0fLs1FUUpKcmy/view?usp=sharing)

#

- Expondo porta, fazendo login (falha) e então criando conta no ***fiap.stock.portal.front***
- Visualizando que não há produtos

[googledrive/2-export-portal.mp4](https://drive.google.com/file/d/1lk-brhzBbSVEIIMU92EeqTwqEnNqE527/view?usp=sharing)

#
 
- Expondo porta, fazendo login (falha) e então criando conta no ***fiap.stock.mgnt.front***
- Cadastrando item no catalogo
- Cadastrando produtos
- Visualizando que não há registros de pedidos

[googledrive/3-export-mgnt.mp4](https://drive.google.com/file/d/1cUBf3mX11RycNHAC0gRsNpZ7cRcCibr9/view?usp=sharing)

#
 
- Volta no ***fiap.stock.portal.front***
- Cadastra um endereço
- Visualiza produtos cadastrados e adiciona produtos ao carrinho
- Faz pedido
  
[googledrive/4-take-an-order.mp4](https://drive.google.com/file/d/1D3UdfVNypd0Ixi25gUn54sIHosc0tfGw/view?usp=sharing)

#
 
- Volta no ***fiap.stock.mgnt.front***
- Visualiza pedidos efetuados
- Estoquista aprova e rejeita pedidos

[googledrive/print-17.png](https://drive.google.com/file/d/1GKNWIWtm5CpFH-Oy0z2wLWExlM_Q3Ci_/view?usp=sharing)

#

- No ***fiap.stock.portal.front***
- O cliente vê o status dos pedidos respondidos

[googledrive/print-18.png](https://drive.google.com/file/d/1LnBrSqKcsabeObvuu5NnfM0cX5jnmWDz/view?usp=sharing)