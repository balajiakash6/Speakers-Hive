// Tab switching (Login / Register)
const tabs = document.querySelectorAll(".tab-btn");
const forms = {
  "login-form": document.getElementById("login-form"),
  "register-form": document.getElementById("register-form"),
};
const formTitle = document.getElementById("form-title");

function showForm(formId) {
  // activate tab
  tabs.forEach((btn) => {
    btn.classList.toggle("active", btn.dataset.target === formId);
  });

  // show correct form
  Object.entries(forms).forEach(([id, form]) => {
    form.classList.toggle("active", id === formId);
  });

  // title text
  formTitle.textContent =
    formId === "login-form" ? "Welcome Back" : "Create your account";
}

// click on tabs
tabs.forEach((btn) => {
  btn.addEventListener("click", () => showForm(btn.dataset.target));
});

// links under the forms
document.getElementById("to-register").addEventListener("click", () => {
  showForm("register-form");
});
document.getElementById("to-login").addEventListener("click", () => {
  showForm("login-form");
});

// --- IMPORTANT PART ---
// For NOW, let the register form submit normally to register.php
// so DO NOT call preventDefault here.

const registerForm = document.getElementById("register-form");
registerForm.addEventListener("submit", () => {
  // no e.preventDefault()
  // browser will go to register.php and run your PHP insert code
});

// For login, we can still block for now (until login.php is ready)
const loginForm = document.getElementById("login-form");
loginForm.addEventListener("submit", (e) => {
  e.preventDefault();
  alert("Login will be connected to login.php later.");
});
