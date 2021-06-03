package cat.udl.eps.bbdd;

import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ListActivity{
	private EditText txtNombre;
	private EditText txtTel;
	private EditText txtMail;
	private TextView textreturn;

	private Button btnInsertar;
	private Button btnActualizar;
	private Button btnEliminar;
	private Button cons_todos;
	private Button cons_1;


	private SQLiteDatabase db;
	private SQLiteDatabase db2;
	private String tableUsuariosName = "Usuarios";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//Obtenemos las referencias a los controles
		txtNombre = (EditText)findViewById(R.id.txtNom);
		txtTel = (EditText)findViewById(R.id.txtTel);
		txtMail = (EditText)findViewById(R.id.txtMail);
		textreturn = (TextView) findViewById(R.id.textreturn);

		btnInsertar = (Button)findViewById(R.id.btnInsertar);
		btnActualizar = (Button)findViewById(R.id.btnActualizar);
		btnEliminar = (Button)findViewById(R.id.btnEliminar);
		cons_todos = (Button) findViewById(R.id.consTodos);
		cons_1 = (Button) findViewById(R.id.cons1);

		//Abrimos la base de datos 'DBUsuarios' en modo escritura
		final UsuariosSQLiteHelper usdbh =
				new UsuariosSQLiteHelper(this, "DBUsuarios", null, 5);

		db = usdbh.getWritableDatabase();
		db2 = usdbh.getReadableDatabase();

		if(db != null) {
			//Insertamos 5 usuarios de ejemplo
			for(int i=1; i<=5; i++){
				//Generamos los datos

				String nombre = "Usuario" + (i+5);
				String telefono = "688-555-981" + (i+5);
				String email = "correo" + (i+5) + "@mail.com";

				ContentValues contentInsert = new ContentValues();
				contentInsert.put("nombre",nombre);
				contentInsert.put("telefono",telefono);
				contentInsert.put("email",email);

				long isInserted = db.insert(tableUsuariosName,null,contentInsert);
				/*if(isInserted != -1)
					Toast.makeText(getApplicationContext(),"Row: "+isInserted+" inserted correctly",Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(getApplicationContext(),"Error inserting new Row",Toast.LENGTH_SHORT).show();*/

				//Insertamos los datos en la tabla Usuarios
                //db.execSQL("INSERT INTO Usuarios (nombre, telefono, email) " +
                           //"VALUES ('" + nombre + "', '" + telefono +"', '" + email + "')");
			}
		}

		btnInsertar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//Recuperamos los valores de los campos de texto
				String nom = txtNombre.getText().toString();
				String tel = txtTel.getText().toString();
				String mail = txtMail.getText().toString();

				ContentValues newsfromEdit = new ContentValues();
				newsfromEdit.put("nombre",nom);
				newsfromEdit.put("telefono",tel);
				newsfromEdit.put("email",mail);
				long isInserted = db.insert(tableUsuariosName,null,newsfromEdit);
				if(isInserted != -1)
					Toast.makeText(getApplicationContext(),"Row "+isInserted+" well inserted",Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(getApplicationContext(),"Error inserting new Row",Toast.LENGTH_SHORT).show();

				//String sql = "INSERT INTO Usuarios (nombre,telefono,email) VALUES ('" + nom + "','" + tel + "','" + mail + "') ";
				//db.execSQL(sql);
			}
		});

		btnActualizar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//Recuperamos los valores de los campos de texto
				String nom = txtNombre.getText().toString();
				String tel = txtTel.getText().toString();
				String mail = txtMail.getText().toString();

				String[] args = new String[]{nom};

				ContentValues contentUpdate = new ContentValues();
				contentUpdate.put("nombre",nom);
				contentUpdate.put("telefono",tel);
				contentUpdate.put("email",mail);

				int rowsAffected = db.update(tableUsuariosName,contentUpdate,"nombre=?",args);
				Toast.makeText(getApplicationContext(),"Total of: "+rowsAffected+" rows updated correctly",Toast.LENGTH_SHORT).show();


				//String sql = "UPDATE Usuarios SET telefono='" + tel + "', email='" + mail + "' WHERE nombre=?";
				//db.execSQL(sql, args);
			}
		});

		btnEliminar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//Recuperamos los valores de los campos de texto
				String nom = txtNombre.getText().toString();
				String[] args = new String[]{nom};

				// Only deleting with name --> "nombre=?"
				int rowsAffected = db.delete(tableUsuariosName,"nombre=?",args);
				Toast.makeText(getApplicationContext(),"Total of: " +rowsAffected+" rows deleted correctly",Toast.LENGTH_SHORT).show();

				//String sql = "DELETE FROM Usuarios WHERE nombre=?";
				//db.execSQL(sql, args);
			}
		});
		cons_todos.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//Recuperamos los valores de los campos de texto

				Cursor cursor = db2.rawQuery("select * from Usuarios",null);

				String tableString = String.format("Results for all table:  %s\n", tableUsuariosName);
				if (cursor.moveToFirst() ){
					String[] columnNames = cursor.getColumnNames();
					do {
						for (String name: columnNames) {
							tableString += String.format("%s: %s\n", name,
									cursor.getString(cursor.getColumnIndex(name)));
						}
						tableString += "\n";

					} while (cursor.moveToNext());
				}
				Toast.makeText(getApplicationContext(),"Showing all Users in the Database, scroll down!!",Toast.LENGTH_SHORT).show();
				textreturn.setText(tableString);
			}
		});
		cons_1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//Recuperamos los valores de los campos de texto
				String nom = txtNombre.getText().toString();
				String[] campos = new String[]{"telefono","email"};
				String[] args = new String[]{nom};

				Cursor cursor = db2.query(tableUsuariosName,campos, "nombre=?",args, null, null, null);
				String tableString = String.format("Results for all table:  %s\n", tableUsuariosName);
				if (cursor.moveToFirst() ){
					String[] columnNames = cursor.getColumnNames();
					do {
						for (String name: columnNames) {
							tableString += String.format("%s: %s\n", name,
									cursor.getString(cursor.getColumnIndex(name)));
						}
						tableString += "\n";

					} while (cursor.moveToNext());
				}
				Toast.makeText(getApplicationContext(),"Showing user: "+nom+" information!",Toast.LENGTH_SHORT).show();
				textreturn.setText(tableString);
				//String sql = "DELETE FROM Usuarios WHERE nombre=?";
				//db.execSQL(sql, args);
			}
		});
	}
	@Override
	protected void onDestroy () {
		super.onDestroy();
		db.close();
	}
}
