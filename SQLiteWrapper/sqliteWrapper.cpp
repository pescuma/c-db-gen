#include <stdio.h>

#include "Sqlite.h"
#include "scope.h"
#include "Product.h"





TCHAR tmp[1024];



void error(const TCHAR *err)
{
	OutputDebugString("\n\n ------ \n");
	OutputDebugString(err);
	OutputDebugString("\n ------ \n\n\n");
}


void assertEquals(int expected, int actual)
{
	if (expected == actual)
		return;

	_snprintf(tmp, 1024, "Expected %d got %d", expected, actual);
	error(tmp);
	exit(-1);
}


class Test
{
public:
	Test()
	{
		OutputDebugString("Test()");
	}

	Test(const Test &t)
	{
		OutputDebugString("Test(const Test &t)");
	}
};


int main(int argc, char* argv[])
{
	sqlite::Database sqlite;

	try {

		sqlite.open("test.db");

		sqlite.execute("DROP TABLE IF EXISTS tmp");

		sqlite.execute("CREATE TABLE tmp (id INTEGER PRIMARY KEY, name VARCHAR)");

		assertEquals(0, sqlite.get("SELECT count(*) FROM tmp"));

		sqlite.execute("INSERT INTO tmp(name) VALUES ('Testetetet ã ')");
		sqlite.execute("INSERT INTO tmp(name) VALUES ('1213')");
		sqlite.execute("INSERT INTO tmp(name) VALUES (NULL)");

		assertEquals(3, sqlite.get("SELECT count(*) FROM tmp"));

		{
			sqlite::Statement stmt = sqlite.prepare("SELECT * FROM tmp");

			while(stmt.step())
				printf("%d\t%s\n", stmt.getColumnAsInt(0), scope<TCHAR *>(stmt.getColumnAsString(1)));
		}

		Product p;
		strcpy(p.code, "a");

		Sell s;
		p.sells().push_back(s);
		Sell &x = p.sells()[0];
		Sell::store(&sqlite, &s);

		Product::createTable(&sqlite);
		Product::store(&sqlite, &p);


		sqlite.close();


	} catch(sqlite::DatabaseException e) {
		error(e.message);
	}

	return 0;
}

