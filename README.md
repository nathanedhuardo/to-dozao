Implementação de histórico de alteração de status da Task

Neste PR foi implementada a funcionalidade de alteração de status de uma Task com registro automático do histórico dessa alteração.

Como funciona o fluxo:

-Quando o status de uma Task é alterado, a mudança é salva no banco de dados.

-Em seguida, é criado um registro em TaskHistory, armazenando:

  • a data da alteração,

  • o novo status,
 
  • uma observação (notes), quando informada.

-Todo o processo é feito de forma transacional, garantindo consistência entre a Task e seu histórico.

Testes:

Para facilitar os testes durante o desenvolvimento, foi utilizado o Postman para validar o endpoint de alteração de status.

Após os testes, códigos que tinham apenas finalidade de teste foram removidos para manter o projeto mais organizado e evitar confusão no código final.

Objetivo da implementação:

Essa funcionalidade permite manter um histórico das mudanças feitas nas Tasks, o que pode ser útil para acompanhamento da evolução das atividades e futuras funcionalidades do sistema.
