// Скрипт для переключения активной вкладки
document.addEventListener("DOMContentLoaded", function () {
    const navLinks = document.querySelectorAll(".nav-link");

    navLinks.forEach(link => {
        link.addEventListener("click", function () {
            // Удаляем класс active у всех ссылок
            navLinks.forEach(nav => nav.classList.remove("active"));
            // Добавляем класс active к текущей ссылке
            this.classList.add("active");
        });
    });
});
