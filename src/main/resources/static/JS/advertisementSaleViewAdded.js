function setupImageUpload(inputId, previewContainerId) {
    const fileInput = document.getElementById(inputId);
    const previewContainer = document.getElementById(previewContainerId);

    if (!fileInput || !previewContainer) return;

    fileInput.addEventListener('change', () => {
        // Очистка контейнера превью
        previewContainer.innerHTML = '';

        const files = fileInput.files;
        if (files.length === 0) return;

        // Создание превью для каждого файла
        Array.from(files).forEach((file) => {
            const reader = new FileReader();

            reader.onload = (e) => {
                const img = document.createElement('img');
                img.src = e.target.result;
                img.className = 'img-thumbnail';
                img.style.maxWidth = '150px';
                img.style.marginRight = '10px';
                previewContainer.appendChild(img);
            };

            reader.readAsDataURL(file);
        });
    });
}
function deleteImage(imageId) {
    fetch(`/realty/advertisementSale/deleteImage/${imageId}`, {
        method: 'DELETE'
    })
        .then(response => {
            if (response.ok) {
                // Удалить изображение из DOM
                document.querySelector(`[data-image-id="${imageId}"]`).remove();
            } else {
                alert('Ошибка при удалении изображения.');
            }
        });
}


function setMainImage(imageId) {
    fetch(`/realty/advertisementSale/setMainImage/${imageId}`, {
        method: 'POST'
    })
        .then(response => {
            if (!response.ok) {
                alert('Ошибка при установке основного изображения.');
            }
        });
}

