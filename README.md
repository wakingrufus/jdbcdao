jdbcdao
=======
This is a very bare-bones lightweight ORM.

What is does:
extending JdbcDao will give you CRUD operations on any object that is at least annotated with an @Id property for the primary Key
It will also expose the findWhereCriteria method which allows building custom WHERE clauses

What it does not do:
it does not joins/foreign key relationships
It does not do paging/sorting


It assumes the primary Key field is auto generated.


