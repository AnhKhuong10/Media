// Danh sách album đã chia sẻ (ảo)
export const albumShare = [
    {
        id: "share1",
        name: "Album đã chia sẻ 1",
        isShared: true,
        createdAt: "2025-10-01T10:00:00Z",
        photos: [
            {
                id: "s1p1",
                url: "https://i.pinimg.com/736x/a1/2c/2e/a12c2e2a63bfcc24229c6518c28912f2.jpg",
                filename: "Ảnh chia sẻ 1",
                liked: false,
                createdAt: "2025-10-01T10:01:00Z",
                isDeleted: false
            },
            {
                id: "s1p2",
                url: "https://letsenhance.io/static/73136da51c245e80edc6ccfe44888a99/396e9/MainBefore.jpg",
                filename: "Ảnh chia sẻ 2",
                liked: true,
                createdAt: "2025-10-01T10:02:00Z",
                isDeleted: false
            }
        ]
    },
    {
        id: "share2",
        name: "Album đã chia sẻ 2",
        isShared: true,
        createdAt: "2025-10-02T11:00:00Z",
        photos: [
            {
                id: "s2p1",
                url: "https://media.istockphoto.com/id/814423752/photo/eye-of-model-with-colorful-art-make-up-close-up.jpg?s=612x612&w=0&k=20&c=l15OdMWjgCKycMMShP8UK94ELVlEGvt7GmB_esHWPYE=",
                filename: "Ảnh chia sẻ 3",
                liked: false,
                createdAt: "2025-10-02T11:01:00Z",
                isDeleted: false
            }
        ]
    }
];
export const photoData ={
    "data": {
        "getUserPhotos": [
            {
                "id": "1",
                "url": "https://letsenhance.io/static/73136da51c245e80edc6ccfe44888a99/396e9/MainBefore.jpg",
                "filename": "img1",
                "liked": true,
                "createdAt": "2025-09-28T11:42:40.806042Z",
                "isDeleted": false
            },
            {
                "id": "2",
                "url": "https://media.istockphoto.com/id/814423752/photo/eye-of-model-with-colorful-art-make-up-close-up.jpg?s=612x612&w=0&k=20&c=l15OdMWjgCKycMMShP8UK94ELVlEGvt7GmB_esHWPYE=",
                "filename": "img 2",
                "liked": false,
                "createdAt": "2025-09-28T13:51:18.264598Z",
                "isDeleted": false
            },
            {
                "id": "3",
                "url": "https://media.istockphoto.com/id/814423752/photo/eye-of-model-with-colorful-art-make-up-close-up.jpg?s=612x612&w=0&k=20&c=l15OdMWjgCKycMMShP8UK94ELVlEGvt7GmB_esHWPYE=",
                "filename": null,
                "liked": false,
                "createdAt": "2025-09-28T15:17:52.681029Z",
                "isDeleted": false
            },
            {
                "id": "4",
                "url": "https://i.pinimg.com/736x/a1/2c/2e/a12c2e2a63bfcc24229c6518c28912f2.jpg",
                "filename": "user 1,",
                "liked": false,
                "createdAt": "2025-09-28T15:17:59.104424Z",
                "isDeleted": false
            },
            {
                "id": "5",
                "url": "https://i.pinimg.com/736x/a1/2c/2e/a12c2e2a63bfcc24229c6518c28912f2.jpg",
                "filename": "user 2s,",
                "liked": false,
                "createdAt": "2025-09-28T15:25:14.892637Z",
                "isDeleted": false
            },
            {
                "id": "6",
                "url": "https://i.pinimg.com/736x/a1/2c/2e/a12c2e2a63bfcc24229c6518c28912f2.jpg",
                "filename": "user 2sss,",
                "liked": false,
                "createdAt": "2025-09-28T15:28:05.236354Z",
                "isDeleted": false
            }
        ]
    }
}



///////////// 
export const DataAlbumUser ={
    "data": {
        "getUserAlbums": [
            {
                "id": "1",
                "name": "album1",
                "createdAt": "1999-12-31T17:00Z",
                "photos": [
                    {
                        "id": "1",
                        "url": "https://letsenhance.io/static/73136da51c245e80edc6ccfe44888a99/396e9/MainBefore.jpg",
                        "filename": "img1",
                        "liked": true,
                        "createdAt": "2025-09-28T11:42:40.806042Z",
                        "isDeleted": false
                    }
                ]
            },
            {
                "id": "2",
                "name": "11",
                "createdAt": "2025-09-28T11:42:40.806042Z",
                "photos": []
            },
            {
                "id": "3",
                "name": "album 2",
                "createdAt": "2025-09-28T14:03:14.027558Z",
                "photos": []
            },
            {
                "id": "4",
                "name": "4ssssssss",
                "createdAt": "2025-09-28T14:05:29.246845Z",
                "photos": []
            }
        ]
    }
}


export const userList={
    "data":{
        "getUsers":[{
           "id":"1",
           "name":"name1"

        },{
           "id":"12",
           "name":"name2"

        },{
           "id":"3",
           "name":"name3"

        },{
           "id":"4",
           "name":"name4"

        },]
    }
}