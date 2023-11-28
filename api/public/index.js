const container = document.querySelector('.container');
const logInLink = document.querySelector('.SingInLink');
const registerLink = document.querySelector('.SingUpLink');

registerLink.addEventListener('click', () => {
    container.classList.add('active')
});

logInLink.addEventListener('click', () => {
    container.classList.remove('active');
})