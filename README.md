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

	driver=<postgres/sqlite3/mysql/db2>
	db=<path_to_database>
	host=<host_name>
	user=<user_name>

## Sample Query
SSQL queries begin with the keyword GENERATE followed by a TFE and the regular FROM and WHERE clauses as required by SQL. For example, consider a table item with following schema:
    

The SSQL query:

	GENERATE HTML
	[ s.city@{align=“center”, bgcolor=“navy”, color=“white”}!
   	[d.floor, [d.name, [i.name]! ]! ]!
	],@{bgcolor=“cyan”, color=“blue”}
	FROM shop s, dept d, item i
	WHERE i.dept=d.id AND d.shop=s.id
		
creates a html document out of the results from the SQL query.

## Visit the lab website [here](http://www.db.ics.keio.ac.jp/).
