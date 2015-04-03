using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SmartGroceryApiLibrary.Models
{
    public class User
    {
        public string Username { get; set; }
        public string Password { get; set; }
        public string AUTH_KEY { get; set; }
        public string SESS_KEY { get; set; }
        public string SESS_EXPIRY { get; set; }
    }
}
