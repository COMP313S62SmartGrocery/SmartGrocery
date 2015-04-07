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

        /* Methods related to Notification table */

        [OperationContract]
        [WebInvoke(UriTemplate = "notifications/get", Method = "POST")]
        List<Notification> GetNotifications(User user);

        [OperationContract]
        [WebInvoke(UriTemplate = "notifications/getCount", Method = "POST")]
        string GetNotificationsCount(User user);

        [OperationContract]
        [WebInvoke(UriTemplate = "notifications/setRead", Method = "POST", BodyStyle=WebMessageBodyStyle.WrappedRequest)]
        bool SetNotificationAsRead(User user, int notificationId);

        [OperationContract]
        [WebInvoke(UriTemplate = "notifications/update", Method = "POST", BodyStyle=WebMessageBodyStyle.WrappedRequest)]
        bool UpdateNotification(User user, int notificationId, string text);

        [OperationContract]
        [WebInvoke(UriTemplate = "notifications/delete", Method = "POST", BodyStyle=WebMessageBodyStyle.WrappedRequest)]
        bool DeleteNotification(User user, int notificationId);

        /* Methods related to ItemHistory table */

        [OperationContract]
        [WebInvoke(UriTemplate = "history/getItems", Method = "POST")]
        List<string> GetItems(User user);

        [OperationContract]
        [WebInvoke(UriTemplate = "history/get", Method = "POST", BodyStyle = WebMessageBodyStyle.WrappedRequest)]
        List<ItemHistory> GetItemHistory(User user, string itemName);

        [OperationContract]
        [WebInvoke(UriTemplate = "history/clear", Method = "POST", BodyStyle = WebMessageBodyStyle.WrappedRequest)]
        bool ClearItemHistory(User user, string itemName);

        /* Methods related to list table */
        [OperationContract]
        [WebInvoke(UriTemplate = "list/", Method = "POST")]
        List<List> GetLists(User user);

        [OperationContract]
        [WebInvoke(UriTemplate = "list/getLastModified", Method = "POST")]
        string GetLastModified(User user, string listId);

        [OperationContract]
        [WebInvoke(UriTemplate = "list/add", Method = "POST", BodyStyle = WebMessageBodyStyle.WrappedRequest)]
        string AddList(List list, User user);

        [OperationContract]
        [WebInvoke(UriTemplate = "list/rename", Method = "POST", BodyStyle = WebMessageBodyStyle.WrappedRequest)]
        bool RenameList(long listId, string newName, User user);

        [OperationContract]
        [WebInvoke(UriTemplate = "list/share", Method = "POST", BodyStyle = WebMessageBodyStyle.WrappedRequest)]
        bool ShareList(long listId, string withUsername, User user);


        [OperationContract]
        [WebInvoke(UriTemplate = "list/unshare", Method = "POST", BodyStyle = WebMessageBodyStyle.WrappedRequest)]
        bool UnshareList(long listId, string withUsername, User user);

        [OperationContract]
        [WebInvoke(UriTemplate = "list/duplicate", Method = "POST", BodyStyle = WebMessageBodyStyle.WrappedRequest)]
        List DuplicateList(long listId, string listName, User user);

        [OperationContract]
        [WebInvoke(UriTemplate = "list/delete", Method = "POST", BodyStyle = WebMessageBodyStyle.WrappedRequest)]
        bool DeleteList(long listId, User user);
        
        /* Methods related to listitem table */
        [OperationContract]
        [WebInvoke(UriTemplate = "listitems/", Method = "POST", BodyStyle=WebMessageBodyStyle.WrappedRequest)]
        List<ListItem> GetListItems(User user, string listId);

        [OperationContract]
        [WebInvoke(UriTemplate = "listitems/count", Method = "POST", BodyStyle = WebMessageBodyStyle.WrappedRequest)]
        int GetListItemsCount(User user, string listId);
        
        [OperationContract]
        [WebInvoke(UriTemplate = "listitems/add", Method = "POST", BodyStyle = WebMessageBodyStyle.WrappedRequest)]
        string AddListItem(User user, ListItem listItem);

        [OperationContract]
        [WebInvoke(UriTemplate = "listitems/add", Method = "POST", BodyStyle = WebMessageBodyStyle.WrappedRequest)]
        bool UpdateListItem(User user, ListItem listItem);

        [OperationContract]
        [WebInvoke(UriTemplate = "listitems/delete", Method = "POST", BodyStyle = WebMessageBodyStyle.WrappedRequest)]
        bool DeleteListItem(User user, string itemId, string time);
    }
}
