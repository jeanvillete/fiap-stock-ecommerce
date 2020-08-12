###### Escola: FIAP
###### Curso: MBA FULLSTACK DEVELOPER, MICROSERVICES, CLOUD & IoT
###### Aluno / RM: JEAN BRUNO SOUTO VILLETE / 335435
###### Prof.: CARLOS VINICIUS MAGNABOSCHI HESKETH

# 1 - INTEGRATIONS & DEVELOPMENT TOOLS

O projeto em questão atende ao trabalho para avaliação da disciplina **Integration and Development Tools**, que
 compreende o desenvolvimento de um aplicativo desenvolvido em módulos que se comunicam assincronamente através do 
 protocolo AMQP, mais especificamente com o message broker RabbitMQ.

O repositório corrente é uma ideia fictícia do gerenciamento de um estoque e a apresentação dos produtos do estoque em
 um portal, onde há a implementação de microserviços que atendem o estoque com seu backend e frontend específico, e
 outra implementação de microserviços que atendem o portal, também com seu backend e frontend específicos.
 
A cada disciplina/curso dentro do MBA, o conhecimento é aplicado no repositório a fim de agregar no projeto.
 
***A integração entre os dois módulos até este ponto era feita sincronamente, através de requisições RESTFul feitas entre
 eles.***

Logo para o curso corrente agregou em um melhor (mais baixo) acoplamento entre os módulos e a comunicação assincrina
 aplicada através do RabbitMQ, utilizando exchanges (fanout) e filas duráveis, além da gerência manual de 
 **acknowledgments**.

# 2 - CASOS DE USO E COMPONENETES AFETADOS

#### 2.1 - Atualização de produtos no portal

***Antes;***  
Quando o estoquista cadastra produtos participantes de um catálogo dentro do estoque, nós sumarizamos os dados deste(s)
 produto(s) e atualizamos o portal com este novo registro.  
Esta comunicação, antes tinha o controller ``` fiap.stock.portal.product.application.ProductController ``` no portal, 
 expondo um endpoint com POST para novos produtos, com estilo de operação UPSERT (insert or update), e um cliente deste
 endpoint ``` fiap.stock.mgnt.summarizedproduct.domain.SummarizedProductServiceImpl ``` que disparava os dados 
 sumarizados dos produtos via um Spring Rest Template.  

***Depois;***  
A comunicação agora tem o ``` fiap.stock.mgnt.summarizedproduct.domain.SummarizedProductServiceImpl ``` postaando os
 dados sumarizados no exchange ***exchange.updated.product***, e do outro lado, no portal o componente 
 ``` fiap.stock.portal.product.application.ProductInbound ``` recepciona os dados dos produtos sumarizados, e persiste
 na base do portal via mesma rotina que era antes invocada pelo controller, como mencionado acima.

#

#### 2.2 - Solicitação e pedidos no portal devem chegar até o estoquista para aprovação ou rejeição

***Antes;***  
No portal, o cliente consegue interagir com a lista de produtos registrados, selecionar uma série destes produtos, e 
 solicitar um pedido destes produtos, neste passo, o cliente através do componente
 ``` fiap.stock.portal.stockorder.domain.StockOrderServiceImpl ``` montava o payload representando o pedido e fazia uma
 requisição POST através de um Spring Rest Template para o ***fiap.stock.mgnt***, que mantinha o controller 
 ``` fiap.stock.mgnt.order.application.OrderController ``` recepcionando estes pedidos e persistindo em base local, 
 para futura conferência do estoquista, que deveria então aprovar ou rejeitar o(s) pedido(s).  
Uma vez o estoquista na tela de pedidos (pedidos com status aguardando resposta) no ***fiap.stock.mgnt***, o estoquista
 pode selecionar uma das opções Aprovar ou Rejeitar pedido, que por sua vez deveria fazer uma requisição através do 
 cliente ``` fiap.stock.mgnt.portalorder.domain.PortalOrderServiceImpl ``` afim de atualizar o status do pedido no 
 ***fiap.stock.portal*** e permitir o cliente observar o ocorrido, e no portal, esta requisição era exposta através do
  controller ``` fiap.stock.portal.order.application.OrderController ```.

***Depois;***  
Para os pedidos efetuados no portal, estes continuam sendo montados em um payload gerido no artefato
 ``` fiap.stock.portal.stockorder.domain.StockOrderServiceImpl ```, porém agora este coloca o payload no exchange
 ***exchange.requested.order***, que será consumido pelo ``` fiap.stock.mgnt.order.application.OrderInbound ```.  
Já para a atualização de status de pedidos aprovados ou rejeitados, o componente responsável por montar/produzir e 
 postar o payload é o ``` fiap.stock.mgnt.portalorder.domain.PortalOrderServiceImpl ```, que disponibiliza o payload no
 exchange ***exchange.updated.order***, e quem consome do outro lado é o 
 ``` fiap.stock.portal.order.application.OrderInbound ```.