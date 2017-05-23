# SuperSQL
SuperSQL is an extension of the SQL language which allows you to easily and quickly structure the output of a relational database. Moreover, this language provides a large variety of layouts.

## How it works?
SuperSQL extends SQL with __TFE__ (Target Form Expression) to generate various kinds of structured publishing and presentation documents. TFE uses connectors and repeaters to specify the structure of the document generated as a result of the query.

    +--------------+
    |SuperSQL Query|
    +------+-------+
           |
+-----------------------------SuperSQL-----+
|          |                               |
|       +--v---+                           |
|       |Parser+------------->SQL Query+------------+
|       +--+---+                           |        |
|          |              +-----------+    |        |
|          v              |    Data   |    |        |
|        Schema           |Constructor+------>Row   |
|          +              +----+------+    |  Data  |
|          |                   |           |    ^   |
|   +------v-------+           v           |    |   |
|   |Code Generator<------+Structured      |    |   |
|   +---+--+--+----+          data         |  +-v---v----+
|   |   |  |  |                            |  |Relational|
|   |   |  |  |                            |  | Database |
+------------------------------------------+  +----------+
    |   |  |  |
    v   v  |  v
  HTML  XML| Responsive Webpage
           v
          Mobile_HTML5


## Setting up
### Maven
The project is based on Maven.

### Config File:
A config file is required by SuperSQL. Create a config.ssql file in the home directory and add the lines:

`driver=<dbms>`
`db=<path to database>`

## Sample Query
SSQL queries begin with the keyword GENERATE followed by a TFE and the regular FROM and WHERE clauses as required by SQL. For example:

`GENERATE HTML`
`[i.name , i.price]!`
`FROM item i`
`WHERE i.supplier =`
`(SELECT s.id FROM supplier s`
`where s.name = 'リーバンアトラス')`
		
creates a html document out of the results from the SQL query.

## Visit the lab website [here](http://www.db.ics.keio.ac.jp/).