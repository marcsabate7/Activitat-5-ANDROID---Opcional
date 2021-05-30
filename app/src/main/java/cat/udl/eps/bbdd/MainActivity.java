package cat.udl.eps.bbdd;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private EditText txtNombre;
	private EditText txtTel;
	private EditText txtMail;

	private Button btnInsertar;
	private Button btnActualizar;
	private Button btnEliminar;

	private SQLiteDatabase db;
	private String tableUsuariosName = "Usuarios";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//Obtenemos las referencias a los controles
		txtNombre = (EditText)findViewById(R.id.txtNom);
		txtTel = (EditText)findViewById(R.id.txtTel);
		txtMail = (EditText)findViewById(R.id.txtMail);

		btnInsertar = (Button)findViewById(R.id.btnInsertar);
		btnActualizar = (Button)findViewById(R.id.btnActualizar);
		btnEliminar = (Button)findViewById(R.id.btnEliminar);


		//Abrimos la base de datos 'DBUsuarios' en modo escritura
		final UsuariosSQLiteHelper usdbh =
				new UsuariosSQLiteHelper(this, "DBUsuarios", null, 3);

		db = usdbh.getWritableDatabase();

		if(db != null)
		{
			//Insertamos 5 usuarios de ejemplo
			for(int i=1; i<=5; i++)
			{
				//Generamos los datos

				String nombre = "Usuario" + (i+5);
				String telefono = "688-555-981" + (i+5);
				String email = "correo" + (i+5) + "@mail.com";

				ContentValues contentInsert = new ContentValues();
				contentInsert.put("nombre",nombre);
				contentInsert.put("telefono",telefono);
				contentInsert.put("email",email);

				long isInserted = db.insert(tableUsuariosName,null,contentInsert);
				if(isInserted != -1)
					Toast.makeText(getApplicationContext(),"Row "+isInserted+" well inserted",Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(getApplicationContext(),"Error inserting new Row",Toast.LENGTH_SHORT).show();

				//Insertamos los datos en la tabla Usuarios
/*
                db.execSQL("INSERT INTO Usuarios (nombre, telefono, email) " +
                           "VALUES ('" + nombre + "', '" + telefono +"', '" + email + "')");

 */


				db.insert(tableUsuariosName,null,contentInsert);
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

				/*

				String sql = "INSERT INTO Usuarios (nombre,telefono,email) VALUES ('" + nom + "','" + tel + "','" + mail + "') ";
				db.execSQL(sql);

				 */

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
				Toast.makeText(getApplicationContext(),rowsAffected+" rows updated",Toast.LENGTH_SHORT).show();

				/*
				String sql = "UPDATE Usuarios SET telefono='" + tel + "', email='" + mail + "' WHERE nombre=?";
				db.execSQL(sql, args);

				 */

			}
		});

		btnEliminar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				//Recuperamos los valores de los campos de texto
				String nom = txtNombre.getText().toString();
				String[] args = new String[]{nom};

				int rowsAffected = db.delete(tableUsuariosName,"nombre=?",args);
				Toast.makeText(getApplicationContext(),rowsAffected+" rows updated",Toast.LENGTH_SHORT).show();
				/*
				String sql = "DELETE FROM Usuarios WHERE nombre=?";
				db.execSQL(sql, args);

				 */
			}
		});
	}
	@Override
	protected void onDestroy () {
		super.onDestroy();
		db.close();
	}
}
