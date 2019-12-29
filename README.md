# CyberSecurityBase-Project

### Introduction

This is my project for the Cybersecurity Base MOOC at the University of Helsinki. This web application is a deliberately insecure social networking platform, with functionality resembling that of Reddit or Hacker News. 

The logic of the site is fairly simple; users enter the site, and make posts. These posts can be edited at a later time by the user, or an admin, and can also be deleted by the admin. Admin privileges can be given by an admin to any user, but cannot be revoked. There are thus three tiers of users: anonymous browsers, who are limited to browsing the main page, creating new accounts, and logging into the site; users, who are allowed to make posts, delete them, and edit them, and admins, who can do all of the above, as well as delete users and grant admin privileges to other users. Of course, there are several problems that this application has with this logic, and this project will seek to point out and rectify these identified problems. 

To install this application, clone into the repo, and run the program on a local server with maven, by entering the directory through Command Prompt or Terminal, and running the command mvn spring-boot:run. There are two users created when the application initializes for testing purposes, so to do any testing, login as blank with the password pass, or as dash with the password word. For admin access, login as admin with the password admin. Or, if you would prefer, there is also functionality to create your own account.

Identification and Description of Vulnerabilies

Flaw 1 -
Cross Site Request Forgery The first insecurity is a lack of CSRF tokens on the application, leading to a problem in places such as the password changing page of the application. This vulnerability can be noticed Since the application does not even ask you to enter your last password, this is an ideal attack vector for any malicious agents. To test out this vulnerability, I have enclosed an HTML file, which contains a form that acts on the web application, thus showing the vulnerability in action. These forms are on the last two pictures, the ones that look like clickbait ads. If you have Maven configured differently, simply change the URL on which the form acts. Hopefully you will not get around to grading this until I have made the CSS a bit better.

FIX CSRF:

To fix the CSRF problem, enable CSRF tokens in the application. This can be done by commenting out this line: http.csrf().disable(); from the SecurityConfiguration.java file, and by manually adding CSRF tokens, by adding this line of HTML: <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/> to each of the POST forms in this application.

Flaw 2 -
Cross Site Scripting The second insecurity is the usage of unsigned text rather than signed text for displaying user- entered information. This allows users to embed HTML into the page (try entering <script> alert("Hello World"); </script> into one of the new post fields), creating an XSS vulnerability, again opening up an attack vector.

FIX XSS:

To fix the XSS vulnerability, simply change the th:utext attributes in the main template to th:text attributes instead. This treats the inputted text as text, instead of allowing scripting in the input fields.

Flaw 3 -
Insecure Direct Object References This insecurity lies in the fact that the post editing pages are referred to directly by URL, with the url being http://localhost:8080/edit/{the number of the post}. This means that, if a user correctly guesses the number of a post, he or she can edit any post that they would like, not just the ones that they are allowed to. Also, because of the way that the site is set up, it is impossible to determine who edited the post, or even whether the post was edited at all. To test out this vulnerability, try changing the number that comes after the URL extension /change, such as /change/0. It should be noted, however, that only authenticated users are allowed to access the editing page, so you must log in before you try to test this vulnerability.

FIX Insecure Direct Object References:

There are a few ways in which this vulnerability can be solved. The first solution is to check the authentication of the user every time they try to enter the edit post page, so that only the user that created the post, or admins, are allowed to edit a post. The second way to solve this problem is to use a hash when referring to the post, so that it is difficult for an unauthenticated user to guess the URL for a post that they are not authorized to view. However, this solution is difficult, and is not as secure as the previous one, as anyone with access to this link can access this web page, so this method is not recommended.

Flaw 4 -
Missing Function Level Access Control This insecurity exists because any authenticated user is allowed to access the admin page. While only admin level users have a hyperlink to the admin page, any user can access the admin page by simply changing the URL extension to \admin.

FIX Missing Function Level Access Control:

To rectify this vulnerability, one simply needs to change the security configuration in the SecurityConfiguration.java file. Remove "/admin", "/admin/**" from the antMatchers method, and then create a new method "antMatchers("/admin", "\admin/**").hasRole("ADMIN") below. This will check to see if the authenticated user is an admin level user, and only allow them to access the admin page.

Flaw 5 -
Security Misconfiguration This vulnerability exists because of the other vulnerabilities present in the application, notably the CSRF vulnerability, as well as the numerous errors in business logic. Also, you may notice that /h2-console/ is open to all users.

FIX Security Misconfiguration

To fix this vulnerability, one needs to correct all of the previous errors, and fix the business logic of the code. One notable patch for the business logic would be to add a feature making it impossible to revoke admin privileges for one user, and to make it impossible to delete oneself as a user.
