using SmartGroceryApiLibrary.Models;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SmartGroceryApiLibrary.DataAccess
{
    public class ListSet
    {
        public bool AddList(List list)
        {
            ConnectionManager connection = new ConnectionManager();
            connection.Open();

            SqlCommand cmd = new SqlCommand("insert into Templates(NAME,COLOR) values(@name,@color)", connection.con);
            cmd.Parameters.Add(new SqlParameter("@name", list.Name));
            cmd.Parameters.Add(new SqlParameter("@color", list.Color));

            bool ret = cmd.ExecuteNonQuery() > 0;

            connection.Close();

            return ret;
        }

        public Template GetTemplate(int id)
        {
            ConnectionManager connection = new ConnectionManager();
            connection.Open();

            Template template = null;

            SqlCommand cmd = new SqlCommand("Select * from Templates where Id=@id", connection.con);
            cmd.Parameters.Add(new SqlParameter("@id", id));
            SqlDataReader sdr = cmd.ExecuteReader();

            if (sdr.HasRows)
            {
                template = new Template();
                template.Id = id;
                template.Name = sdr["NAME"].ToString();
                template.Color = sdr["COLOR"].ToString();
            }

            sdr.Close();
            connection.Close();

            return template;
        }

        public bool DeleteTemplate(int templateId)
        {
            ConnectionManager connection = new ConnectionManager();
            connection.Open();

            SqlCommand cmd = new SqlCommand("delete from Templates where ID=@id", connection.con);
            cmd.Parameters.Add(new SqlParameter("@id", templateId));

            bool ret = cmd.ExecuteNonQuery() > 0;

            connection.Close();

            return ret;
        }

        public List<Template> GetTemplateList(string query)
        {
            ConnectionManager connection = new ConnectionManager();
            connection.Open();

            List<Template> list = new List<Template>();

            SqlCommand cmd = new SqlCommand("Select * from Templates where Name LIKE '%" + query + "%'", connection.con);
            SqlDataReader sdr = cmd.ExecuteReader();

            if (sdr.HasRows)
            {
                while (sdr.Read())
                {
                    Template template = new Template();
                    template.Id = long.Parse(sdr["Id"].ToString());
                    template.Name = sdr["Name"].ToString();
                    template.Color = sdr["Color"].ToString();

                    list.Add(template);
                }
            }

            sdr.Close();
            connection.Close();

            return list;
        }
    }
}
