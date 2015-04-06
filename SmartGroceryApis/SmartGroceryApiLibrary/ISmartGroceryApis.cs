using SmartGroceryApiLibrary.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;

namespace SmartGroceryApiLibrary
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the interface name "IService1" in both code and config file together.
    [ServiceContract]
    public interface ISmartGroceryApis
    {
        /* Methods related to Users table */
        [OperationContract]
        [WebInvoke(UriTemplate = "user/Register", Method = "POST")]
        string Register(User user);

        [OperationContract]
        [WebInvoke(UriTemplate = "user/Authenticate", Method = "POST")]
        string Authenticate(string authKey);

        [OperationContract]
        [WebInvoke(UriTemplate = "user/GetAuthKey", Method = "POST")]
        string GetAuthKey(User user);

        [OperationContract]
        [WebInvoke(UriTemplate = "user/UpdatePassword", Method = "POST", BodyStyle=WebMessageBodyStyle.WrappedRequest)]
        bool UpdatePassword(User user, string newPassword);


        [OperationContract]
        [WebInvoke(UriTemplate = "user/Delete", Method = "POST")]
        bool Delete(User user);

        /* Methods related to template table */
        [OperationContract]
        [WebInvoke(UriTemplate = "templates?query={query}", Method = "GET")]
        List<Template> GetTemplates(string query);

        [OperationContract]
        [WebInvoke(UriTemplate = "templates/details/{templateId}", Method = "GET")]
        Template GetTemplate(string templateId);

        [OperationContract]
        [WebInvoke(UriTemplate = "templates/{templateId}", Method = "GET")]
        List<TemplateItem> GetTemplateItems(string templateId);

        /* Methods related to list table 
        [OperationContract]
        [WebInvoke(UriTemplate = "list/", Method = "POST", BodyStyle = WebMessageBodyStyle.WrappedRequest)]
        bool GetLists(List<List> list);

        [OperationContract]
        [WebInvoke(UriTemplate = "list/add", Method = "POST", BodyStyle = WebMessageBodyStyle.WrappedRequest)]
        bool AddList(List list, string sessionKey);

        [OperationContract]
        [WebInvoke(UriTemplate = "list/delete", Method = "POST", BodyStyle = WebMessageBodyStyle.WrappedRequest)]
        bool DeleteList(long listId, string sessionKey);

        [OperationContract]
        [WebInvoke(UriTemplate = "list/duplicate", Method = "POST", BodyStyle = WebMessageBodyStyle.WrappedRequest)]
        bool DuplicateList(long listId , string sessionKey);

        [OperationContract]
        [WebInvoke(UriTemplate = "list/rename", Method = "POST", BodyStyle = WebMessageBodyStyle.WrappedRequest)]
        bool RenameList(long listId, string newName, string sessionKey);

        /* Methods related to listitem table */
    }
}
